package com.example.laboratorio.ui.main

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.example.laboratorio.ui.network.Estacion
@Composable
fun MainMenu(
    onLogout: () -> Unit,
    viewModel: MainMenuViewModel = viewModel()
) {
    val state = viewModel.uiState

    var selectedTab by remember { mutableStateOf(MainTab.SCAN) }

    val scanLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        viewModel.onScanResult(result.contents)
    }

    LaunchedEffect(Unit) {
        viewModel.loadUserData()
    }

    // Dialog de resultado de reclamar (éxito / error)
    if (state.reclamarMessage != null || state.reclamarError != null) {
        val isError = state.reclamarError != null
        AlertDialog(
            onDismissRequest = { viewModel.clearReclamarStatus() },
            title = { Text(if (isError) "Error" else "Residuo reclamado") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (isError) {
                        Text(state.reclamarError ?: "Ocurrió un error.")
                    } else {
                        Text(state.reclamarMessage ?: "OK")
                        state.categoria?.let { Text("Categoría: $it") }
                        state.puntos?.let { Text("Puntos: $it") }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { viewModel.clearReclamarStatus() }) {
                    Text("Cerrar")
                }
            }
        )
    }

    val bg = Brush.verticalGradient(
        listOf(
            Color(0xFF0B6B55),
            Color(0xFF0A5A48),
            Color(0xFF08493C)
        )
    )

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            BottomBar(
                selected = selectedTab,
                onSelect = { selectedTab = it },
                onScanClick = {
                    val options = ScanOptions().apply {
                        setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                        setPrompt("Escanea un código QR")
                        setCameraId(0)
                        setBeepEnabled(false)
                        setBarcodeImageEnabled(true)
                        setOrientationLocked(true)
                    }
                    scanLauncher.launch(options)
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                TopHeader(
                    username = state.username ?: "Usuario",
                    level = 5, // HARDCODEADO
                    onLogout = onLogout
                )

                when (selectedTab) {
                    MainTab.STORE -> HomeContent(state)
                    MainTab.SCAN -> {
                        LaunchedEffect(Unit) {
                            viewModel.loadEstaciones()
                        }

                        if (state.isLoading) {
                            CircularProgressIndicator(color = Color.White)
                        } else {
                            // Ahora 'listaEstaciones' ya no debería estar en rojo
                            MapaOsmContent(estaciones = state.listaEstaciones)
                        }
                    }
                    MainTab.RANKING -> HomeContent(state)
                }
            }

            // Overlay cuando está reclamando QR/puntos
            if (state.isClaiming) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun HomeContent(state: MainMenuUiState) {
    when {
        state.isLoading -> {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        }

        state.errorMessage != null -> {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Error", fontWeight = FontWeight.SemiBold, color = Color(0xFFB00020))
                    Spacer(Modifier.height(6.dp))
                    Text(state.errorMessage ?: "Error desconocido")
                }
            }
        }

        else -> {
            PointsCard(points = state.puntosTotales)
            StreakCard(days = 5) // HARDCODEADO
            WeeklyProgressRow(doneCount = 5, total = 7) // HARDCODEADO
        }
    }
}

@Composable
private fun TopHeader(
    username: String,
    level: Int,
    onLogout: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White.copy(alpha = 0.85f), CircleShape)
                    .background(Color(0xFF0F8C6E)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Spacer(Modifier.width(10.dp))

            Column {
                Text(
                    text = "¡Hola, $username!",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.SignalCellularAlt,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Nivel $level",
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HeaderIconButton(icon = Icons.Filled.Notifications, onClick = { /* TODO */ })
            HeaderIconButton(icon = Icons.Filled.Logout, onClick = onLogout)
        }
    }
}

@Composable
private fun HeaderIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.15f))
    ) {
        Icon(icon, contentDescription = null, tint = Color.White)
    }
}

@Composable
private fun PointsCard(points: Int) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF15A37A)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Autorenew, // reciclaje “aprox”
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Puntaje Total",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF4B5563)
            )
            Text(
                text = "$points pts",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F8C6E)
            )

            Spacer(Modifier.height(10.dp))

            AssistChip(
                onClick = { },
                label = { Text("Top 15% de recicladores") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.EmojiEvents,
                        contentDescription = null
                    )
                }
            )
        }
    }
}

@Composable
private fun StreakCard(days: Int) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFEDD5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.LocalFireDepartment,
                    contentDescription = null,
                    tint = Color(0xFFF97316)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$days días consecutivos",
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111827)
                )
                Text(
                    text = "¡Mantén el ritmo!",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFF97316)
                )
            }
        }
    }
}

@Composable
private fun WeeklyProgressRow(doneCount: Int, total: Int) {
    val labels = listOf("D", "L", "M", "M", "J", "V", "S")

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "Progreso semanal",
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF111827)
            )
            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                labels.take(total).forEachIndexed { index, day ->
                    val done = index < doneCount
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(day, style = MaterialTheme.typography.bodySmall, color = Color(0xFF6B7280))
                        Spacer(Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(if (done) Color(0xFF10B981) else Color(0xFFE5E7EB)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (done) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private enum class MainTab { STORE, SCAN, RANKING }

@Composable
private fun BottomBar(
    selected: MainTab,
    onSelect: (MainTab) -> Unit,
    onScanClick: () -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 10.dp
    ) {
        NavigationBarItem(
            selected = selected == MainTab.STORE,
            onClick = { onSelect(MainTab.STORE) },
            icon = { Icon(Icons.Filled.Storefront, contentDescription = null) },
            label = { Text("Tienda") }
        )

        NavigationBarItem(
            selected = selected == MainTab.SCAN,
            onClick = {
                onSelect(MainTab.SCAN)
                onScanClick()
            },
            icon = {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF10B981)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.QrCodeScanner,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            label = { Text("Escanear") },
            alwaysShowLabel = true
        )

        NavigationBarItem(
            selected = selected == MainTab.RANKING,
            onClick = { onSelect(MainTab.RANKING) },
            icon = { Icon(Icons.Filled.Leaderboard, contentDescription = null) },
            label = { Text("Ranking") }
        )
    }
}
