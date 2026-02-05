package com.example.laboratorio.ui.store

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Timer
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
import com.example.laboratorio.ui.main.store.StoreViewModel

// --- COLORES ---
private val GreenPrimary = Color(0xFF00C49A)
private val GreenDarker = Color(0xFF008F7A)
private val OrangeGradientStart = Color(0xFFFF9800)
private val OrangeGradientEnd = Color(0xFFFF5722)
private val PurpleGradientStart = Color(0xFFE040FB)
private val PurpleGradientEnd = Color(0xFFD500F9)
private val TealGradientStart = Color(0xFF1DE9B6)
private val TealGradientEnd = Color(0xFF00BFA5)

@Composable
fun StoreScreen(
    onBack: () -> Unit,
    viewModel: StoreViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    // Fondo general verde degradado
    val mainBg = Brush.verticalGradient(
        listOf(GreenPrimary, GreenDarker)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(mainBg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // 1. HEADER (Título + Wallet)
            Column(
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                // Barra Superior
                StoreTopBar(onBack)

                Spacer(modifier = Modifier.height(20.dp))

                // Tarjeta de Monedas
                WalletCard(points = state.points)

                Spacer(modifier = Modifier.height(30.dp))
            }

            // 2. LISTA DE PRODUCTOS
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(Color(0xFFF5F7F8)) // Fondo gris muy claro
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.items) { item ->
                        // Lógica visual basada en el nombre
                        val (gradientColors, icon, typeTag, duration) = determineVisuals(item.name)

                        StoreItemCard(
                            name = item.name,
                            description = item.description,
                            cost = item.cost,
                            userPoints = state.points,
                            icon = icon,
                            gradientColors = gradientColors,
                            typeTag = typeTag,
                            duration = duration,
                            isPopular = item.name.contains("2x"),
                            isOwned = item.isOwned, // <--- Estado de comprado
                            onBuy = { viewModel.buyItem(item) }
                        )
                    }
                    // Espacio extra al final
                    item { Spacer(Modifier.height(40.dp)) }
                }
            }
        }
    }
}

// ----------------------------------------------------------------
// COMPONENTES UI
// ----------------------------------------------------------------

@Composable
fun StoreTopBar(onBack: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                .size(40.dp)
        ) {
            Icon(Icons.Default.ArrowBack, "Volver", tint = Color.White)
        }

        Spacer(Modifier.width(16.dp))

        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFFFC107), CircleShape)
                .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.ShoppingBag, null, tint = Color.White, modifier = Modifier.size(24.dp))
        }

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                "Tienda Eco",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                "Mejora tu experiencia",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun WalletCard(points: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono Moneda Grande
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color(0xFFFFC107), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.MonetizationOn, null, tint = Color.White, modifier = Modifier.size(32.dp))
            }

            Spacer(Modifier.width(16.dp))

            Column {
                Text("Tus Monedas Disponibles", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "$points",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )
                    Icon(
                        Icons.Default.AutoAwesome,
                        null,
                        tint = Color(0xFFFFD54F),
                        modifier = Modifier.size(20.dp).padding(start = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun StoreItemCard(
    name: String,
    description: String,
    cost: Int,
    userPoints: Int,
    icon: ImageVector,
    gradientColors: List<Color>,
    typeTag: String,
    duration: String,
    isPopular: Boolean,
    isOwned: Boolean,
    onBuy: () -> Unit
) {
    val canBuy = userPoints >= cost && !isOwned

    Card(
        modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Row(modifier = Modifier.padding(16.dp)) {
                // 1. IMAGEN DEL ITEM (Izquierda)
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Brush.linearGradient(gradientColors)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(Modifier.width(16.dp))

                // 2. DETALLES DEL ITEM (Derecha)
                Column(modifier = Modifier.weight(1f)) {
                    // Header: Título + Badge Popular
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f),
                            maxLines = 1
                        )
                        if (isPopular) {
                            Surface(
                                color = Color(0xFFFF9800),
                                shape = RoundedCornerShape(bottomStart = 8.dp, topEnd = 8.dp)
                            ) {
                                Row(Modifier.padding(horizontal = 6.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Star, null, tint = Color.White, modifier = Modifier.size(10.dp))
                                    Text("Popular", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(4.dp))
                    Text(description, color = Color.Gray, fontSize = 12.sp, lineHeight = 16.sp, maxLines = 2)

                    Spacer(Modifier.height(8.dp))

                    // Tags: Tipo y Duración
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TagChip(typeTag, Color(0xFFFFE0B2), Color(0xFFE65100))
                        Spacer(Modifier.width(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Timer, null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                            Spacer(Modifier.width(2.dp))
                            Text(duration, color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }
            }

            // 3. FOOTER: Efecto y Precio/Botón
            Divider(color = Color.Gray.copy(alpha = 0.1f))

            Column(modifier = Modifier.padding(16.dp)) {
                // Caja de Efecto (Verde claro)
                Surface(
                    color = Color(0xFFE0F2F1), // Verde muy claro
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.AutoAwesome, null, tint = GreenDarker, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = if(name.contains("2x")) "+2x Puntos por reciclaje" else if (name.contains("Suerte")) "+30% Suerte" else if(name.contains("Logro")) "Insignia desbloqueable" else "Beneficio activo",
                            color = GreenDarker,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Fila de Compra
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Precio (Ocultar si ya está comprado)
                    if (!isOwned) {
                        Surface(
                            color = Color(0xFFFFF8E1), // Amarillo muy claro
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFECB3))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.MonetizationOn, null, tint = Color(0xFFFFA000), modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("$cost", color = Color(0xFF5D4037), fontWeight = FontWeight.Bold)
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    // Botón Comprar
                    Button(
                        onClick = onBuy,
                        enabled = canBuy,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isOwned) Color.Gray else if (canBuy) GreenPrimary else Color.LightGray
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp)
                    ) {
                        if (isOwned) {
                            Icon(Icons.Default.Check, null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Comprado")
                        } else {
                            Text("Comprar", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TagChip(text: String, bgColor: Color, textColor: Color) {
    Surface(color = bgColor, shape = RoundedCornerShape(6.dp)) {
        Text(
            text = text,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        )
    }
}

// --- HELPERS Y DATA CLASS (IMPORTANTE: Esto faltaba) ---

fun determineVisuals(itemName: String): VisualData {
    return when {
        itemName.contains("Booster", ignoreCase = true) -> VisualData(
            gradient = listOf(Color(0xFFFF9800), Color(0xFFFF5722)),
            icon = Icons.Default.Bolt,
            tag = "Booster",
            duration = "1 hora"
        )
        itemName.contains("Protector", ignoreCase = true) -> VisualData(
            gradient = listOf(Color(0xFF42A5F5), Color(0xFF1976D2)),
            icon = Icons.Default.Security,
            tag = "Seguridad",
            duration = "Único uso"
        )
        itemName.contains("Estrella", ignoreCase = true) -> VisualData(
            gradient = listOf(Color(0xFFE040FB), Color(0xFFD500F9)),
            icon = Icons.Default.AutoAwesome,
            tag = "Suerte",
            duration = "7 días"
        )
        itemName.contains("Logro", ignoreCase = true) -> VisualData(
            gradient = listOf(Color(0xFFFFD700), Color(0xFFFFA000)),
            icon = Icons.Default.EmojiEvents,
            tag = "Insignia",
            duration = "Permanente"
        )
        else -> VisualData(
            gradient = listOf(GreenPrimary, GreenDarker),
            icon = Icons.Default.ShoppingBag,
            tag = "Item",
            duration = "-"
        )
    }
}

// La clase de datos necesaria para que funcione 'determineVisuals'
data class VisualData(
    val gradient: List<Color>,
    val icon: ImageVector,
    val tag: String,
    val duration: String
)