package com.example.laboratorio.ui.store

import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.laboratorio.ui.main.store.MysteryPrizeResult
import com.example.laboratorio.ui.main.store.PrizeType
import kotlinx.coroutines.delay

@Composable
fun MysteryBoxDialog(
    prize: MysteryPrizeResult,
    onDismiss: () -> Unit
) {
    // Estados de la animación
    var isSpinning by remember { mutableStateOf(true) }
    var currentIcon by remember { mutableStateOf(Icons.Default.HelpOutline) }
    var currentIconColor by remember { mutableStateOf(Color.Gray) }

    // Lista de íconos posibles para la "ruleta"
    val possibleIcons = listOf(
        Icons.Default.MonetizationOn to Color(0xFFFFC107), // Moneda
        Icons.Default.Face to Color(0xFF29B6F6),           // Avatar
        Icons.Default.SentimentDissatisfied to Color.Gray  // Nada/Poco
    )

    // Lógica de Animación
    LaunchedEffect(Unit) {
        // Fase 1: Girar rápido (1.5 segundos)
        val spinDuration = 1500L
        val startTime = System.currentTimeMillis()

        while (System.currentTimeMillis() - startTime < spinDuration) {
            val (icon, color) = possibleIcons.random()
            currentIcon = icon
            currentIconColor = color
            delay(100) // Cambia cada 100ms
        }

        // Fase 2: Mostrar el resultado final
        isSpinning = false

        // Asignar icono final según el premio real
        when (prize.type) {
            PrizeType.COINS -> {
                currentIcon = Icons.Default.MonetizationOn
                currentIconColor = Color(0xFFFFC107)
            }
            PrizeType.AVATAR -> {
                currentIcon = Icons.Default.Face
                currentIconColor = Color(0xFFAB47BC)
            }
            PrizeType.EMPTY -> {
                currentIcon = Icons.Default.SentimentDissatisfied
                currentIconColor = Color.Gray
            }
        }
    }

    // Animación de escala (latido)
    val scale by animateFloatAsState(
        targetValue = if (isSpinning) 1f else 1.2f, // Se agranda al terminar
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Dialog(onDismissRequest = { /* No cerrar clickeando afuera mientras gira */ }) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if(isSpinning) "Abriendo caja..." else "¡Resultado!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEC407A)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Círculo con el ícono cambiante
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .scale(scale)
                        .border(4.dp, currentIconColor, CircleShape)
                        .background(currentIconColor.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = currentIcon,
                        contentDescription = null,
                        tint = currentIconColor,
                        modifier = Modifier.size(50.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (!isSpinning) {
                    // Mostrar texto del premio
                    Text(
                        text = prize.message,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    if (prize.type == PrizeType.COINS) {
                        Text("+${prize.amount}", color = Color(0xFFFFC107), fontWeight = FontWeight.Black, fontSize = 24.sp)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEC407A))
                    ) {
                        Text("¡Genial!")
                    }
                } else {
                    // Espaciador para que no salte el layout
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }
    }
}