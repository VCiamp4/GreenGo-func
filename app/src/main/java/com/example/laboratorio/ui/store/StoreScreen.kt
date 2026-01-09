package com.example.laboratorio.ui.store

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StoreScreen(
    points: Int
) {
    val viewModel = remember { StoreViewModel(points) }
    val state by viewModel.uiState.collectAsState()

    if (state.successMessage != null || state.errorMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearMessages() },
            title = {
                Text(if (state.errorMessage != null) "Error" else "Compra exitosa")
            },
            text = {
                Text(state.errorMessage ?: state.successMessage!!)
            },
            confirmButton = {
                Button(onClick = { viewModel.clearMessages() }) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Tus puntos")
                Text(
                    "${state.points} pts",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF0F8C6E)
                )
            }
        }

        state.items.forEach { item ->
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .padding(14.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(item.icon, null, tint = Color(0xFF0F8C6E))
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.name, fontWeight = FontWeight.SemiBold)
                        Text(item.description, style = MaterialTheme.typography.bodySmall)
                        Text("${item.cost} pts", color = Color(0xFF0F8C6E))
                    }
                    Button(
                        onClick = { viewModel.buy(item) },
                        enabled = !state.isBuying
                    ) {
                        Text("Comprar")
                    }
                }
            }
        }

        if (state.isBuying) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
