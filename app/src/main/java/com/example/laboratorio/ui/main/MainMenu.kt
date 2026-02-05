package com.example.laboratorio.ui.main

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

// Imports de tus otras pantallas (Mantenlos igual)
import com.example.laboratorio.ui.store.StoreScreen
import com.example.laboratorio.ui.ranking.RankingScreen

// Definición de Colores del Diseño (Figma)
private val GreenPrimary = Color(0xFF00C49A) // El verde vibrante del header
private val GreenDarker = Color(0xFF00A884)
private val BackgroundColor = Color(0xFFF5F7F8) // Fondo gris muy claro
private val CardBeige = Color(0xFFFFF8E7) // Fondo de la tarjeta de racha
private val FireOrange = Color(0xFFFF5722) // Naranja del fuego

@Composable
fun MainMenu(
    onLogout: () -> Unit,
    onAchievementsClick: () -> Unit,
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

    // --- DIALOGO DE RESULTADO (Igual que antes) ---
    if (state.reclamarMessage != null || state.reclamarError != null) {
        val isError = state.reclamarError != null
        AlertDialog(
            onDismissRequest = { viewModel.clearReclamarStatus() },
            title = {
                Text(
                    text = if (isError) "Error" else "Residuo reclamado",
                    color = if (isError) Color(0xFFB00020) else GreenPrimary
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
                        containerColor = if (isError) Color(0xFFB00020) else GreenPrimary
                    )
                ) {
                    Text("Cerrar")
                }
            }
        )
    }

    Scaffold(
        containerColor = BackgroundColor,
        bottomBar = {
            BottomBar(
                selected = selectedTab,
                onSelect = {
                    selectedTab = it
                    if (it != MainTab.SCAN) showMap = false
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
                .padding(padding)
        ) {
            // Fondo Curvo Verde Superior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp) // Altura del fondo verde
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(GreenPrimary, GreenDarker)
                        )
                    )
            )

            // Contenido Principal
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Header (Dentro del área verde)
                TopHeaderFigmaStyle(
                    username = state.username ?: "Usuario",
                    level = 5,
                    onAchievementsClick = onAchievementsClick, // <--- PASARLO AQUÍ
                    onLogout = onLogout
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Área de contenido scrollable
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 20.dp)
                ) {
                    when (selectedTab) {
                        MainTab.STORE -> StoreScreen(onBack = { selectedTab = MainTab.SCAN })
                        MainTab.RANKING -> RankingScreen(onBack = { selectedTab = MainTab.SCAN })
                        MainTab.SCAN -> {
                            if (!showMap) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(20.dp),
                                    modifier = Modifier.verticalScroll(rememberScrollState())
                                ) {
                                    // Tarjeta de Puntos Principal
                                    PointsCardFigma(state.puntosTotales)

                                    // Tarjeta de Racha y Días (Combinada)
                                    StreakAndProgressCard(daysConsecutive = 5)

                                    // Botón Mapa (Opcional, estilo simple para no romper diseño)
                                    Button(
                                        onClick = { showMap = true },
                                        modifier = Modifier.fillMaxWidth().height(50.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                                    ) {
                                        Icon(Icons.Filled.Map, null, tint = GreenDarker)
                                        Spacer(Modifier.width(8.dp))
                                        Text("Ver estaciones cercanas", color = GreenDarker)
                                    }

                                    Spacer(modifier = Modifier.height(20.dp))
                                }
                            } else {
                                // Mapa
                                MapSection(state, viewModel, onClose = { showMap = false })
                            }
                        }
                    }
                }
            }

            // Loading Overlay
            if (state.isClaiming || state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        }
    }
}

// --- COMPONENTES VISUALES ESTILO FIGMA ---

@Composable
private fun TopHeaderFigmaStyle(
    username: String,
    level: Int,
    onAchievementsClick: () -> Unit, // <--- Nuevo parámetro para la navegación
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        // Fila Superior: Avatar y Botones de Acción
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // --- AVATAR CON BADGE ---
            Box {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .border(2.dp, Color.White, CircleShape)
                        .clip(CircleShape)
                        .background(Color(0xFF0F8C6E)), // Color de fallback si no hay foto
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = username.take(1).uppercase(),
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                // Badge amarilla (Estrella)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 4.dp, y = 4.dp)
                        .size(24.dp)
                        .background(Color(0xFFFFC107), CircleShape)
                        .border(2.dp, GreenPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            // --- BOTONES DE ACCIÓN (Logros, Notificaciones, Config) ---
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                // 1. Botón de Logros (NUEVO)
                HeaderIconButton(
                    icon = Icons.Default.EmojiEvents,
                    onClick = onAchievementsClick
                )

                // 2. Botón de Notificaciones (Solo visual por ahora)
                HeaderIconButton(
                    icon = Icons.Outlined.Notifications,
                    onClick = { /* Acción futura */ }
                )

                // 3. Botón de Configuración (Logout)
                HeaderIconButton(
                    icon = Icons.Outlined.Settings,
                    onClick = onLogout
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SALUDO ---
        Text(
            text = "¡Hola, $username!",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))

        // --- PILL DE NIVEL ---
        Surface(
            color = Color.White.copy(alpha = 0.2f),
            shape = RoundedCornerShape(50)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Eco, null, tint = Color.White, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(4.dp))
                Text("Nivel $level", color = Color.White, fontSize = 14.sp)
            }
        }
    }
}

// Asegúrate de tener este helper también en el archivo (o al final del mismo)
@Composable
private fun HeaderIconButton(icon: ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.15f))
            .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick), // Hacemos clickeable el box
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = Color.White)
    }
}

@Composable
private fun PointsCardFigma(points: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(32.dp)),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ícono circular grande
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(GreenPrimary.copy(alpha = 0.1f)), // Verde muy suave de fondo
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Autorenew, // Icono de reciclaje
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = GreenPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Puntaje Total",
                color = Color.Gray,
                fontSize = 16.sp
            )

            Text(
                text = "$points pts",
                color = GreenPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Divider(modifier = Modifier.padding(horizontal = 40.dp).alpha(0.1f))

            Spacer(modifier = Modifier.height(16.dp))

            // Footer "Top 15%"
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.EmojiEvents,
                    contentDescription = null,
                    tint = Color(0xFFE2B93B),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Top 15% de recicladores",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun StreakAndProgressCard(daysConsecutive: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardBeige) // Color crema
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Fila Superior: Icono Fuego y Texto
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Caja naranja con fuego
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .shadow(8.dp, RoundedCornerShape(12.dp), spotColor = FireOrange)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFFFF8A65), FireOrange)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.LocalFireDepartment,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "$daysConsecutive días consecutivos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF5D4037) // Marrón oscuro
                    )
                    Text(
                        text = "¡Mantén el ritmo!",
                        fontSize = 14.sp,
                        color = FireOrange
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Fila Inferior: Días de la semana (D L M X J)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val days = listOf("D", "L", "M", "X", "J")
                // Simulación de estado: Los primeros 5 completados
                days.forEachIndexed { index, day ->
                    DayIndicator(day = day, isCompleted = true)
                }
                // Si quisieras agregar S y D vacíos podrías
            }
        }
    }
}

@Composable
private fun DayIndicator(day: String, isCompleted: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = day,
            color = if (isCompleted) GreenDarker else Color.Gray,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (isCompleted) {
            // Caso completado: Tiene icono, necesita contentAlignment
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(GreenPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        } else {
            // CORRECCIÓN AQUÍ:
            // Caso pendiente: Es solo un circulo vacio, quitamos el contentAlignment
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFD54F)) // Amarillo
            )
        }
    }
}

// --- BARRA DE NAVEGACIÓN INFERIOR ---

@Composable
private fun BottomBar(
    selected: MainTab,
    onSelect: (MainTab) -> Unit,
    onScanClick: () -> Unit
) {
    // Elevación y fondo blanco limpio
    Surface(
        shadowElevation = 16.dp,
        color = Color.White
    ) {
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 0.dp
        ) {
            // --- BOTÓN TIENDA ---
            NavigationBarItem(
                selected = selected == MainTab.STORE,
                onClick = { onSelect(MainTab.STORE) },
                icon = {
                    Icon(
                        Icons.Filled.Storefront,
                        null,
                        tint = if (selected == MainTab.STORE) GreenPrimary else Color.Gray
                    )
                },
                label = {
                    Text(
                        "Tienda",
                        color = if (selected == MainTab.STORE) GreenPrimary else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )

            // --- BOTÓN CENTRAL (DINÁMICO: QR o HOME) ---
            val isHome = selected == MainTab.SCAN

            NavigationBarItem(
                selected = isHome,
                onClick = {
                    if (isHome) onScanClick() // Si ya estoy en home, abro el escáner
                    else onSelect(MainTab.SCAN) // Si estoy en otro lado, vuelvo al home
                },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(GreenPrimary)
                            .shadow(8.dp, RoundedCornerShape(16.dp), spotColor = GreenPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        // AQUÍ ESTÁ EL CAMBIO:
                        // Si estoy en Home -> Muestro QR
                        // Si NO estoy en Home -> Muestro la Casita
                        Icon(
                            imageVector = if (isHome) Icons.Filled.QrCodeScanner else Icons.Filled.Home,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = if (isHome) "Escanear" else "Inicio",
                        color = GreenPrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )

            // --- BOTÓN RANKING ---
            NavigationBarItem(
                selected = selected == MainTab.RANKING,
                onClick = { onSelect(MainTab.RANKING) },
                icon = {
                    Icon(
                        Icons.Filled.EmojiEvents,
                        null,
                        tint = if (selected == MainTab.RANKING) GreenPrimary else Color.Gray
                    )
                },
                label = {
                    Text(
                        "Ranking",
                        color = if (selected == MainTab.RANKING) GreenPrimary else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )
        }
    }
}

// --- SECCIÓN DE MAPA (Lógica mantenida, estilo actualizado mínimamente) ---

@Composable
private fun MapSection(state: MainMenuUiState, viewModel: MainMenuViewModel, onClose: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Cabecera simple oscura para el modo mapa (o podrías hacerlo flotante)
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Mapa de Estaciones", color = GreenDarker, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, null, tint = Color.Gray)
            }
        }

        LaunchedEffect(Unit) {
            viewModel.loadEstaciones()
        }

        Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                // Aquí va tu componente de mapa original
                MapaOsmContent(estaciones = state.listaEstaciones, viewModel)
            }

            if (state.distanciaRuta != null) {
                Surface(
                    color = GreenPrimary,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(24.dp)
                        .shadow(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.DirectionsWalk, null, tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "${state.distanciaRuta} • ${state.tiempoRuta}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

private enum class MainTab { STORE, SCAN, RANKING }

// Extensión helper para divider alpha si no usas Material3 completo con alpha
fun Modifier.alpha(alpha: Float) = this.then(Modifier.background(Color.Black.copy(alpha = alpha)))