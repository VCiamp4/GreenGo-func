package com.example.laboratorio.ui.achievements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.laboratorio.ui.UserRepository // Asegúrate de importar esto

// --- COLORES ---
private val GreenPrimary = Color(0xFF00C49A)
private val GreenDarker = Color(0xFF008F7A)
private val Gold = Color(0xFFFFD700)
private val LockedGray = Color(0xFFBDBDBD)

// --- MODELOS ---
data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    // Eliminamos 'isUnlocked' del modelo base, porque eso es dinámico
    val type: AchievementType
)

enum class AchievementType { SCAN, SHOP }

@Composable
fun AchievementsScreen(
    onBack: () -> Unit
) {
    // 1. Observamos los logros desbloqueados del Repositorio
    val unlockedIds by UserRepository.unlockedAchievements.collectAsState()

    // 2. Definimos la lista estática de logros (TODOS)
    val allAchievements = remember {
        listOf(
            Achievement("scan_1", "Primer Impacto", "Escanea tu primer código QR.", Icons.Default.QrCodeScanner, AchievementType.SCAN),
            Achievement("scan_5", "Reciclador Constante", "Escanea 5 códigos QR.", Icons.Default.Loop, AchievementType.SCAN),
            Achievement("scan_20", "Experto Ecológico", "Escanea 20 códigos QR.", Icons.Default.Forest, AchievementType.SCAN),

            // Estos IDs coinciden con los de la Tienda
            Achievement("shop_novato", "Comprador Novato", "Adquiere tu primer ítem en la tienda.", Icons.Default.ShoppingBag, AchievementType.SHOP),
            Achievement("shop_compulsivo", "Comprador Compulsivo", "Gasta más de 1000 monedas.", Icons.Default.ShoppingCart, AchievementType.SHOP),
            Achievement("shop_coleccionista", "Coleccionista", "Ten al menos 5 ítems activos.", Icons.Default.Collections, AchievementType.SHOP)
        )
    }

    // Calculamos el progreso real
    val unlockedCount = allAchievements.count { unlockedIds.contains(it.id) }

    Scaffold(
        containerColor = Color(0xFFF5F7F8),
        topBar = {
            AchievementTopBar(onBack, unlockedCount, allAchievements.size)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Barra de Progreso
            item {
                LinearProgressIndicator(
                    progress = if (allAchievements.isNotEmpty()) unlockedCount / allAchievements.size.toFloat() else 0f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(50)),
                    color = GreenPrimary,
                    trackColor = Color.LightGray.copy(alpha = 0.5f)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "$unlockedCount de ${allAchievements.size} logros desbloqueados",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))
            }

            items(allAchievements) { achievement ->
                // Pasamos 'isUnlocked' calculándolo en tiempo real
                AchievementCard(
                    item = achievement,
                    isUnlocked = unlockedIds.contains(achievement.id)
                )
            }
        }
    }
}

@Composable
fun AchievementTopBar(onBack: () -> Unit, unlocked: Int, total: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(Brush.verticalGradient(listOf(GreenPrimary, GreenDarker)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .size(40.dp)
            ) {
                Icon(Icons.Default.ArrowBack, "Volver", tint = Color.White)
            }

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.EmojiEvents, null, tint = Gold, modifier = Modifier.size(32.dp))
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Mis Logros",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun AchievementCard(item: Achievement, isUnlocked: Boolean) {
    // Lógica visual basada en si está desbloqueado
    val containerColor = if (isUnlocked) Color.White else Color.White.copy(alpha = 0.6f)
    val contentColor = if (isUnlocked) Color.Black else Color.Gray

    val iconBg = if (isUnlocked) {
        if (item.type == AchievementType.SCAN) GreenPrimary.copy(alpha = 0.1f) else Gold.copy(alpha = 0.1f)
    } else {
        Color.Gray.copy(alpha = 0.1f)
    }

    val iconTint = if (isUnlocked) {
        if (item.type == AchievementType.SCAN) GreenPrimary else Color(0xFFFFA000)
    } else {
        LockedGray
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(if (isUnlocked) 4.dp else 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isUnlocked) item.icon else Icons.Default.Lock,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = contentColor
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isUnlocked) Color.Gray else LockedGray
                )
            }

            if (isUnlocked) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = GreenPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}