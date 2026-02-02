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

// Asegúrate de que estas rutas coincidan con la estructura de carpetas de tu compa
import com.example.laboratorio.ui.store.StoreScreen
import com.example.laboratorio.ui.ranking.RankingScreen

@Composable
fun MainMenu(
    onLogout: () -> Unit,
    viewModel: MainMenuViewModel = viewModel()
) {
    val state = viewModel.uiState
    var selectedTab by remember { mutableStateOf(MainTab.SCAN) }
    var showMap by remember { mutableStateOf(false) }

    val scanLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        viewModel.onScanResult(result.contents)
    }

    LaunchedEffect(Unit) {
        viewModel.loadUserData()
    }

    // Dialog de resultado al reclamar QR (Estilo unificado)
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
        listOf(Color(0xFF0B6B55), Color(0xFF0A5A48), Color(0xFF08493C))
    )

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            BottomBar(
                selected = selectedTab,
                onSelect = {
                    selectedTab = it
                    if (it != MainTab.SCAN) showMap = false // Resetea el mapa al cambiar de tab
                },
                onScanClick = {
                    val options = ScanOptions().apply {
                        setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                        setPrompt("Escanea un código QR")
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
                    level = 5,
                    onLogout = onLogout
                )

                when (selectedTab) {
                    MainTab.STORE -> {
                        StoreScreen(onBack = { selectedTab = MainTab.SCAN })
                    }

                    MainTab.RANKING -> {
                        RankingScreen(onBack = { selectedTab = MainTab.SCAN })
                    }

                    MainTab.SCAN -> {
                        if (!showMap) {
                            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                HomeContent(state)
                                Button(
                                    onClick = { showMap = true },
                                    modifier = Modifier.fillMaxWidth().height(56.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF15A37A))
                                ) {
                                    Icon(Icons.Filled.Map, null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("Ver estaciones de reciclaje")
                                }
                            }
                        } else {
                            // Contenedor del Mapa con info de Ruta
                            MapSection(state, viewModel, onClose = { showMap = false })
                        }
                    }
                }
            }

            if (state.isClaiming) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun MapSection(state: MainMenuUiState, viewModel: MainMenuViewModel, onClose: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Mapa de Estaciones", color = Color.White, fontWeight = FontWeight.Bold)
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, null, tint = Color.White)
            }
        }

        LaunchedEffect(Unit) {
            viewModel.loadEstaciones()
        }

        Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(18.dp)
            ) {
                MapaOsmContent(estaciones = state.listaEstaciones, viewModel)
            }

            // Datos de distancia y tiempo flotando sobre el mapa
            if (state.distanciaRuta != null) {
                Surface(
                    color = Color.Black.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.DirectionsWalk, null, tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "${state.distanciaRuta} • ${state.tiempoRuta}",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
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
            StreakCard(days = 5)
            WeeklyProgressRow(doneCount = 5, total = 7)
        }
    }
}

@Composable
private fun TopHeader(username: String, level: Int, onLogout: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0F8C6E))
                    .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Person, null, tint = Color.White)
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text("¡Hola, $username!", color = Color.White, fontWeight = FontWeight.Bold)
                Text("Nivel $level", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
            }
        }
        IconButton(
            onClick = onLogout,
            modifier = Modifier.clip(CircleShape).background(Color.White.copy(alpha = 0.1f))
        ) {
            Icon(Icons.Filled.Logout, null, tint = Color.White)
        }
    }
}

@Composable
private fun PointsCard(points: Int) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Filled.Autorenew, null, tint = Color(0xFF0F8C6E), modifier = Modifier.size(32.dp))
            Text("Puntos totales", style = MaterialTheme.typography.bodyMedium)
            Text("$points pts", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color(0xFF0F8C6E))
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
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.LocalFireDepartment, null, tint = Color(0xFFF97316))
            Spacer(Modifier.width(10.dp))
            Text("$days días consecutivos", fontWeight = FontWeight.Medium)
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
            Text("Progreso semanal", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                labels.take(total).forEachIndexed { index, day ->
                    val done = index < doneCount
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(day, style = MaterialTheme.typography.bodySmall)
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(if (done) Color(0xFF10B981) else Color(0xFFE5E7EB)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (done) Icon(Icons.Filled.Check, null, tint = Color.White, modifier = Modifier.size(16.dp))
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
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = selected == MainTab.STORE,
            onClick = { onSelect(MainTab.STORE) },
            icon = { Icon(Icons.Filled.Storefront, null) },
            label = { Text("Tienda") }
        )
        NavigationBarItem(
            selected = selected == MainTab.SCAN,
            onClick = {
                if (selected == MainTab.SCAN) onScanClick()
                else onSelect(MainTab.SCAN)
            },
            icon = {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selected == MainTab.SCAN) Color(0xFF10B981) else Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (selected == MainTab.SCAN) Icons.Filled.QrCodeScanner else Icons.Filled.Home,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            label = { Text(if (selected == MainTab.SCAN) "Escanear" else "Inicio") }
        )
        NavigationBarItem(
            selected = selected == MainTab.RANKING,
            onClick = { onSelect(MainTab.RANKING) },
            icon = { Icon(Icons.Filled.Leaderboard, null) },
            label = { Text("Ranking") }
        )
    }
}