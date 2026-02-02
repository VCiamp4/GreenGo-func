package com.example.laboratorio.ui.store

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laboratorio.ui.main.store.StoreViewModel

@Composable
fun StoreScreen(
    onBack: () -> Unit,
    viewModel: StoreViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    val bg = Brush.verticalGradient(
        listOf(
            Color(0xFF0F9D58),
            Color(0xFF0B7D46)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
    ) {

        /* ---------- HEADER ---------- */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.18f),
                shadowElevation = 6.dp,
                modifier = Modifier.size(44.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    text = "Tienda Eco",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Mejora tu experiencia",
                    color = Color.White.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        /* ---------- MONEDAS ---------- */
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.18f)
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Tus monedas", color = Color.White.copy(alpha = 0.85f))
                    Text(
                        "${state.points}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                AssistChip(
                    onClick = { /* futuro */ },
                    label = { Text("Ganar más") }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        /* ---------- CONTENIDO ---------- */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color(0xFFF4F7F6),
                    RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                )
                .padding(16.dp)
        ) {

            Text(
                "Boosters",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(12.dp))

            state.items.forEach { item ->
                BoosterCard(
                    name = item.name,
                    description = item.description,
                    cost = item.cost,
                    userPoints = state.points,
                    onBuy = { viewModel.buyItem(item) }
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun BoosterCard(
    name: String,
    description: String,
    cost: Int,
    userPoints: Int,
    onBuy: () -> Unit
) {
    val canBuy = userPoints >= cost

    Card(
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFFFF9800), Color(0xFFFF5722))
                            ),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(name, fontWeight = FontWeight.Bold)
                    Text(
                        description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Button(
                onClick = onBuy,
                enabled = canBuy,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (canBuy) Color(0xFF0F9D58) else Color(0xFF9CA3AF)
                )
            ) {
                Text(
                    text = "Comprar · $cost monedas",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
