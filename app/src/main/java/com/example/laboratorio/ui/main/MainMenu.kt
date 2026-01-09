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
import com.example.laboratorio.ui.store.StoreScreen

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

    // Dialog resultado de reclamar QR
    if (state.reclamarMessage != null || state.reclamarError != null) {
        val isError = state.reclamarError != null

        AlertDialog(
            onDismissRequest = { viewModel.clearReclamarStatus() },
            title = {
                Text(
                    text = if (isError) "Error" else "Residuo reclamado",
                    color = if (isError) Color(0xFFB00020) else Color(0xFF0F8C6E)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (isError) {
                        Text(state.reclamarError ?: "Ocurrió un error.")
                    } else {
                        Text(state.reclamarMessage ?: "OK")
                        state.categoria?.let { Text("Categoría: $it") }
                        state.puntos?.let { Text("Puntos ganados: $it") }
                        Text(
                            text = "Total actual: ${state.puntosTotales} pts",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.clearReclamarStatus() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isError) Color(0xFFB00020) else Color(0xFF0F8C6E)
                    )
                ) {
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
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                TopHeader(
                    username = state.username ?: "Usuario",
                    level = 5, // hardcodeado
                    onLogout = onLogout
                )

                when (selectedTab) {
                    MainTab.STORE -> {
                        StoreScreen(
                            points = state.puntosTotales
                        )
                    }

                    MainTab.SCAN -> {
                        HomeContent(state)
                    }

                    MainTab.RANKING -> {
                        // Por ahora lo dejamos igual
                        HomeContent(state)
                    }
                }
            }

            // Overlay de carga al reclamar QR
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
                    Text(
                        "Error",
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFB00020)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(state.errorMessage)
                }
            }
        }

        else -> {
            PointsCard(points = state.puntosTotales)
            StreakCard(days = 5)
            WeeklyProgressRow(doneCount = 5, total = 7)
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
                Icon(Icons.Filled.Person, null, tint = Color.White)
            }

            Spacer(Modifier.width(10.dp))

            Column {
                Text(
                    "¡Hola, $username!",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Nivel $level",
                    color = Color.White.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HeaderIconButton(Icons.Filled.Notifications) {}
            HeaderIconButton(Icons.Filled.Logout, onLogout)
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
        Icon(icon, null, tint = Color.White)
    }
}

@Composable
private fun PointsCard(points: Int) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(10.dp),
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
                Icon(Icons.Filled.Autorenew, null, tint = Color.White)
            }

            Spacer(Modifier.height(10.dp))

            Text("Puntos totales")
            Text(
                "$points pts",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F8C6E)
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
            Icon(Icons.Filled.LocalFireDepartment, null, tint = Color(0xFFF97316))
            Spacer(Modifier.width(10.dp))
            Text("$days días consecutivos")
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
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            labels.take(total).forEachIndexed { index, day ->
                val done = index < doneCount
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(day)
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(if (done) Color(0xFF10B981) else Color(0xFFE5E7EB))
                    )
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
    NavigationBar {
        NavigationBarItem(
            selected = selected == MainTab.STORE,
            onClick = { onSelect(MainTab.STORE) },
            icon = { Icon(Icons.Filled.Storefront, null) },
            label = { Text("Tienda") }
        )

        NavigationBarItem(
            selected = selected == MainTab.SCAN,
            onClick = {
                onSelect(MainTab.SCAN)
                onScanClick()
            },
            icon = { Icon(Icons.Filled.QrCodeScanner, null) },
            label = { Text("Escanear") }
        )

        NavigationBarItem(
            selected = selected == MainTab.RANKING,
            onClick = { onSelect(MainTab.RANKING) },
            icon = { Icon(Icons.Filled.Leaderboard, null) },
            label = { Text("Ranking") }
        )
    }
}
