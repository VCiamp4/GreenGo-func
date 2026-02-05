package com.example.laboratorio.ui.ranking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

// --- COLORES DEL DISEÑO ---
private val GreenPrimary = Color(0xFF00C49A)
private val GreenDarker = Color(0xFF00A884)
private val BackgroundColor = Color(0xFFF0F4F8)
private val GoldColor = Color(0xFFFFC107)
private val SilverColor = Color(0xFF9E9E9E)
private val BronzeColor = Color(0xFFCD7F32)

@Composable
fun RankingScreen(
    onBack: () -> Unit,
    viewModel: RankingViewModel = viewModel()
) {
    val state = viewModel.uiState

    // TODO: Reemplaza esto con el usuario real de tu Auth/ViewModel
    val currentUsername = "sciamparella"

    // Lógica para encontrar tu posición y puntos reales en la lista cargada
    val myUserIndex = state.items.indexOfFirst { it.username == currentUsername }
    val myUserItem = state.items.getOrNull(myUserIndex)

    val myRank = if (myUserIndex >= 0) myUserIndex + 1 else 0
    val myPoints = myUserItem?.totalPuntos ?: 0

    LaunchedEffect(state.period, state.mode) {
        viewModel.loadRanking()
    }

    Scaffold(
        containerColor = BackgroundColor
    ) { padding ->
        // SOLUCIÓN 1: Quitamos el padding del Box padre para que el verde llegue arriba
        Box(
            modifier = Modifier
                .fillMaxSize()
            // .padding(padding) <--- ESTO SE QUITÓ PARA ELIMINAR LA BARRA BLANCA
        ) {
            // Fondo superior verde (Curvo)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp) // Un poco más alto para cubrir bien
                    .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(GreenPrimary, Color(0xFF008F7A))
                        )
                    )
            )

            // Contenido Scrollable
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                // Aplicamos el padding aquí para que el contenido no quede tapado por la navbar abajo
                contentPadding = PaddingValues(
                    top = padding.calculateTopPadding(),
                    bottom = 20.dp
                )
            ) {
                // 1. Cabecera y Tarjeta Personal
                item {
                    HeaderSection(onBack)

                    // SOLUCIÓN 2: Pasamos los datos calculados dinámicamente
                    MyPositionCard(
                        position = myRank,
                        username = currentUsername,
                        points = myPoints,
                        level = 5, // Si tienes nivel en el objeto User, úsalo aquí: myUserItem?.level ?: 1
                        streakDays = 12 // Lo mismo para racha
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // 2. Tabs (Global / Semanal / Amigos)
                item {
                    CustomSegmentedControl(
                        currentPeriod = state.period,
                        onPeriodSelected = { viewModel.setPeriod(it) }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // 3. Podio (Top 3)
                if (!state.isLoading && state.items.isNotEmpty()) {
                    item {
                        PodiumSection(topThree = state.items.take(3))
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // 4. Lista del resto (Del 4 en adelante)
                if (state.isLoading) {
                    item {
                        Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = GreenPrimary)
                        }
                    }
                } else {
                    // Tomamos del 4to en adelante (índice 3)
                    val restOfList = if (state.items.size > 3) state.items.drop(3) else emptyList()

                    itemsIndexed(restOfList) { index, user ->
                        // La posición real es index + 4
                        val realPosition = index + 4
                        RankingListRow(
                            position = realPosition,
                            username = user.username,
                            points = user.totalPuntos,
                            level = 8,
                            days = 20,
                            isMe = user.username == currentUsername
                        )
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------------------
// COMPONENTES UI PRINCIPALES
// ----------------------------------------------------------------

@Composable
fun HeaderSection(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botón Back y Título
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Icono Trofeo Grande
        Icon(
            imageVector = Icons.Default.EmojiEvents,
            contentDescription = null,
            tint = Color(0xFFFFD54F),
            modifier = Modifier.size(60.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Ranking de Recicladores",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Compite con otros usuarios",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun MyPositionCard(position: Int, username: String, points: Int, level: Int, streakDays: Int) {
    // Si la posición es 0 (no encontrado), mostramos un guión
    val positionText = if (position > 0) "#$position" else "-"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .shadow(10.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F9D58))
    ) {
        Box {
            // Fondo con gradiente sutil
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Brush.linearGradient(listOf(Color(0xFF0F9D58), Color(0xFF0B8043))))
            )

            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(54.dp)
                            .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                            .padding(4.dp)
                            .background(Color(0xFF054D40), CircleShape)
                    ) {
                        Text(username.take(1).uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Info Central
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Tu Posición", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            BadgePill(text = "Nivel $level", color = Color.White.copy(alpha = 0.2f))
                            Spacer(Modifier.width(6.dp))
                            Icon(Icons.Default.Bolt, null, tint = Color(0xFFFFD54F), modifier = Modifier.size(16.dp))
                            Text(" $streakDays días", color = Color.White, fontSize = 12.sp)
                        }
                    }

                    // Posición Grande y Puntos
                    Column(horizontalAlignment = Alignment.End) {
                        Text(positionText, color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                        Text("$points pts", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Footer
                ContainerTranslucido {
                    Icon(Icons.Default.TrendingUp, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("+5 posiciones esta semana", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun CustomSegmentedControl(currentPeriod: RankingPeriod, onPeriodSelected: (RankingPeriod) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(48.dp),
        shape = RoundedCornerShape(50),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RankingPeriod.values().forEach { period ->
                val isSelected = currentPeriod == period
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(50))
                        .background(if (isSelected) Color(0xFFF3F4F6) else Color.Transparent)
                        .clickableNoRipple { onPeriodSelected(period) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = period.label,
                        color = if (isSelected) Color.Black else Color.Gray,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PodiumSection(topThree: List<RankingItem>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        // 2do Lugar (Izquierda)
        if (topThree.size >= 2) {
            PodiumItem(user = topThree[1], rank = 2, color = SilverColor, height = 120.dp, modifier = Modifier.weight(1f))
        } else { Spacer(Modifier.weight(1f)) }

        // 1er Lugar (Centro)
        if (topThree.isNotEmpty()) {
            PodiumItem(user = topThree[0], rank = 1, color = GoldColor, height = 150.dp, isWinner = true, modifier = Modifier.weight(1.2f))
        }

        // 3er Lugar (Derecha)
        if (topThree.size >= 3) {
            PodiumItem(user = topThree[2], rank = 3, color = BronzeColor, height = 120.dp, modifier = Modifier.weight(1f))
        } else { Spacer(Modifier.weight(1f)) }
    }
}

@Composable
fun PodiumItem(user: RankingItem, rank: Int, color: Color, height: Dp, isWinner: Boolean = false, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isWinner) {
            Icon(Icons.Default.EmojiEvents, null, tint = Color(0xFFFFD54F), modifier = Modifier.size(24.dp))
        }

        Spacer(Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .size(if (isWinner) 70.dp else 50.dp)
                .border(3.dp, color, CircleShape)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = user.username.take(2).uppercase(),
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                fontSize = if (isWinner) 20.sp else 16.sp
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 10.dp)
                    .size(24.dp)
                    .background(color, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("$rank", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(user.username, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
        Text("${user.totalPuntos} pts", fontSize = 12.sp, color = GreenPrimary)
    }
}

@Composable
fun RankingListRow(position: Int, username: String, points: Int, level: Int, days: Int, isMe: Boolean) {
    val bgColor = if (isMe) Color(0xFFE8F5E9) else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if(isMe) 0.dp else 2.dp),
        border = if (isMe) BorderStroke(1.dp, GreenPrimary) else null
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Posición
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(if (isMe) GreenPrimary else Color(0xFFF3F4F6), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$position",
                    fontWeight = FontWeight.Bold,
                    color = if (isMe) Color.White else Color.Gray,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(if (isMe) GreenPrimary.copy(alpha = 0.2f) else Color(0xFFE0E0E0), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(username.take(2).uppercase(), color = if (isMe) GreenDarker else Color.Gray, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Datos
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = username,
                        fontWeight = FontWeight.Bold,
                        color = if (isMe) GreenDarker else Color.Black
                    )
                    if (isMe) {
                        Spacer(Modifier.width(6.dp))
                        BadgePill("Tú", GreenPrimary)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Nivel $level", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Default.Bolt, null, tint = Color(0xFFFFD54F), modifier = Modifier.size(12.dp))
                    Text(" $days días", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }

            // Puntos
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$points",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (isMe) GreenDarker else Color.Gray
                )
                Text("puntos", fontSize = 10.sp, color = Color.Gray)
            }
        }
    }
}

// ----------------------------------------------------------------
// FUNCIONES DE AYUDA (HELPERS)
// ----------------------------------------------------------------

@Composable
fun BadgePill(text: String, color: Color) {
    Surface(color = color, shape = RoundedCornerShape(4.dp)) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White
        )
    }
}

@Composable
fun ContainerTranslucido(content: @Composable RowScope.() -> Unit) {
    Box(
        modifier = Modifier
            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, content = content)
    }
}

@Composable
fun Modifier.clickableNoRipple(onClick: () -> Unit): Modifier = this.then(
    Modifier.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
)