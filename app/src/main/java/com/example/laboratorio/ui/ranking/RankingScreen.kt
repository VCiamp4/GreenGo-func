package com.example.laboratorio.ui.ranking

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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

    // Determinamos la unidad visualmente
    val unitSuffix = if (state.mode == RankingMode.RESIDUO) "kg" else "pts"

    // TODO: Reemplaza esto con el usuario real
    val currentUsername = "sciamparella"

    // Buscamos tu usuario en la lista
    val myUserIndex = state.items.indexOfFirst { it.username == currentUsername }
    val myUserItem = state.items.getOrNull(myUserIndex)
    val myRank = if (myUserIndex >= 0) myUserIndex + 1 else 0
    val myPoints = myUserItem?.totalPuntos ?: 0

    Scaffold(
        containerColor = BackgroundColor
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {

            // Fondo superior verde (Curvo)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
                    .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(GreenPrimary, Color(0xFF008F7A))
                        )
                    )
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = padding.calculateTopPadding(),
                    bottom = 20.dp
                )
            ) {
                // 1. Cabecera y Tarjeta Personal
                item {
                    HeaderSection(onBack)

                    MyPositionCard(
                        position = myRank,
                        username = currentUsername,
                        points = myPoints,
                        suffix = unitSuffix,
                        level = 5,
                        streakDays = 12
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // 2. Tabs de Periodo (Global / Semanal)
                item {
                    CustomSegmentedControl(
                        currentPeriod = state.period,
                        onPeriodSelected = { viewModel.setPeriod(it) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 3. Filtros de Modo y Material (DISEÑO MEJORADO)
                item {
                    FilterSection(
                        currentMode = state.mode,
                        currentResidue = state.selectedResidue,
                        onModeSelected = { mode ->
                            viewModel.setMode(mode)
                        },
                        onResidueSelected = { residue ->
                            viewModel.setResiduo(residue)
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // 4. Podio (Top 3)
                if (!state.isLoading && state.items.isNotEmpty()) {
                    item {
                        PodiumSection(
                            topThree = state.items.take(3),
                            suffix = unitSuffix
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // 5. Lista del resto
                if (state.isLoading) {
                    item {
                        Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color.White) // Blanco para que se vea sobre verde o gris
                        }
                    }
                } else if (state.errorMessage != null) {
                    item {
                        Box(Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                            Text(state.errorMessage ?: "Error", color = Color.White)
                        }
                    }
                } else {
                    val restOfList = if (state.items.size > 3) state.items.drop(3) else emptyList()

                    itemsIndexed(restOfList) { index, user ->
                        val realPosition = index + 4
                        RankingListRow(
                            position = realPosition,
                            username = user.username,
                            points = user.totalPuntos,
                            suffix = unitSuffix,
                            level = 8, // Dato mockeado
                            days = 20, // Dato mockeado
                            isMe = user.username == currentUsername
                        )
                    }

                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
}

// ----------------------------------------------------------------
// COMPONENTES DE FILTRO (MEJORADOS)
// ----------------------------------------------------------------

@Composable
fun FilterSection(
    currentMode: RankingMode,
    currentResidue: String?,
    onModeSelected: (RankingMode) -> Unit,
    onResidueSelected: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {

        // Contenedor principal del Switch "Puntos vs Residuo"
        // Usamos un fondo oscuro semitransparente para dar contraste sobre el verde
        Surface(
            color = Color.Black.copy(alpha = 0.2f), // Fondo oscurito para contraste
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier.padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterTypeButton(
                    text = "Puntos Generales",
                    isSelected = currentMode == RankingMode.PUNTOS,
                    onClick = { onModeSelected(RankingMode.PUNTOS) }
                )
                FilterTypeButton(
                    text = "Por Residuo",
                    isSelected = currentMode == RankingMode.RESIDUO,
                    onClick = { onModeSelected(RankingMode.RESIDUO) }
                )
            }
        }

        // Sub-filtros de materiales
        AnimatedVisibility(
            visible = currentMode == RankingMode.RESIDUO,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                Spacer(modifier = Modifier.height(12.dp))
                // Contenedor para los chips de materiales
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    val materiales = listOf("CARTON" to "Cartón", "PAPEL" to "Papel", "PLASTICO" to "Plástico")

                    materiales.forEach { (apiValue, label) ->
                        FilterChipMaterial(
                            text = label,
                            isSelected = currentResidue == apiValue,
                            onClick = { onResidueSelected(apiValue) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterTypeButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    // Si está seleccionado: Fondo Blanco, Texto Verde.
    // Si NO está seleccionado: Fondo Transparente, Texto Blanco (se ve bien sobre el fondo oscuro del padre).
    val backgroundColor = if (isSelected) Color.White else Color.Transparent
    val textColor = if (isSelected) GreenPrimary else Color.White.copy(alpha = 0.9f)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickableNoRipple(onClick)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun FilterChipMaterial(text: String, isSelected: Boolean, onClick: () -> Unit) {
    // Chips de materiales: Amarillo si seleccionado, Blanco semitransparente si no
    val bgColor = if (isSelected) Color(0xFFFFC107) else Color.White.copy(alpha = 0.3f)
    val textColor = if (isSelected) Color.Black else Color.White

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(50),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

// ----------------------------------------------------------------
// RESTO DE COMPONENTES (Header, Tarjetas, etc. - Mantenidos igual)
// ----------------------------------------------------------------

@Composable
fun HeaderSection(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart).background(Color.White.copy(alpha = 0.2f), CircleShape)
            ) { Icon(Icons.Default.ArrowBack, "Volver", tint = Color.White) }
        }
        Icon(Icons.Default.EmojiEvents, null, tint = Color(0xFFFFD54F), modifier = Modifier.size(50.dp))
        Text("Ranking de Recicladores", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun MyPositionCard(position: Int, username: String, points: Int, suffix: String, level: Int, streakDays: Int) {
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
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Brush.linearGradient(listOf(Color(0xFF0F9D58), Color(0xFF0B8043))))
            )

            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
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

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Tu Posición", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            BadgePill("Nivel $level", Color.White.copy(alpha = 0.2f))
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(positionText, color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                        Text("$points $suffix", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomSegmentedControl(currentPeriod: RankingPeriod, onPeriodSelected: (RankingPeriod) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).height(44.dp),
        shape = RoundedCornerShape(50),
        color = Color.White
    ) {
        Row(modifier = Modifier.padding(4.dp)) {
            RankingPeriod.values().forEach { period ->
                val isSelected = currentPeriod == period
                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(50))
                        .background(if (isSelected) Color(0xFFF3F4F6) else Color.Transparent)
                        .clickableNoRipple { onPeriodSelected(period) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(period.label, color = if (isSelected) Color.Black else Color.Gray, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun PodiumSection(topThree: List<com.example.laboratorio.ui.ranking.RankingItem>, suffix: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        if (topThree.size >= 2) PodiumItem(topThree[1], 2, SilverColor, 110.dp, suffix, Modifier.weight(1f))
        else Spacer(Modifier.weight(1f))

        if (topThree.isNotEmpty()) PodiumItem(topThree[0], 1, GoldColor, 140.dp, suffix, Modifier.weight(1.2f), true)

        if (topThree.size >= 3) PodiumItem(topThree[2], 3, BronzeColor, 110.dp, suffix, Modifier.weight(1f))
        else Spacer(Modifier.weight(1f))
    }
}

@Composable
fun PodiumItem(user: com.example.laboratorio.ui.ranking.RankingItem, rank: Int, color: Color, height: Dp, suffix: String, modifier: Modifier, isWinner: Boolean = false) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        if (isWinner) Icon(Icons.Default.EmojiEvents, null, tint = GoldColor, modifier = Modifier.size(24.dp))
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .size(if (isWinner) 65.dp else 50.dp)
                .border(3.dp, color, CircleShape)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(user.username.take(2).uppercase(), fontWeight = FontWeight.Bold, fontSize = if (isWinner) 18.sp else 14.sp)
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
        Text(user.username, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
        Text("${user.totalPuntos} $suffix", fontSize = 10.sp, color = GreenPrimary)
    }
}

@Composable
fun RankingListRow(position: Int, username: String, points: Int, suffix: String, level: Int, days: Int, isMe: Boolean) {
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
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("$position", fontWeight = FontWeight.Bold, color = if (isMe) GreenPrimary else Color.Gray, modifier = Modifier.width(30.dp))

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(if (isMe) GreenPrimary.copy(alpha = 0.2f) else Color(0xFFE0E0E0), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(username.take(2).uppercase(), color = if (isMe) GreenDarker else Color.Gray, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(username, fontWeight = FontWeight.Bold, color = if (isMe) GreenDarker else Color.Black)
                Text("Nivel $level", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }

            Text("$points $suffix", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = if (isMe) GreenDarker else Color.Gray)
        }
    }
}

// Helpers
@Composable
fun BadgePill(text: String, color: Color) {
    Surface(color = color, shape = RoundedCornerShape(4.dp)) {
        Text(text, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = Color.White)
    }
}

@Composable
fun ContainerTranslucido(content: @Composable RowScope.() -> Unit) {
    Box(modifier = Modifier.background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp)).padding(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, content = content)
    }
}

@Composable
fun Modifier.clickableNoRipple(onClick: () -> Unit): Modifier = this.then(
    Modifier.clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClick)
)