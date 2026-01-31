package com.example.laboratorio.ui.main.store

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun StoreScreen(
    viewModel: StoreViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Puntos disponibles: ${state.points}",
            style = MaterialTheme.typography.titleMedium
        )

        state.items.forEach { item ->
            Card {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(item.name, style = MaterialTheme.typography.titleSmall)
                    Text(item.description)
                    Text("Costo: ${item.cost} pts")

                    Button(
                        onClick = { viewModel.buyItem(item) },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Comprar")
                    }
                }
            }
        }
    }

    // Feedback
    state.errorMessage?.let {
        AlertDialog(
            onDismissRequest = viewModel::clearMessages,
            title = { Text("Error") },
            text = { Text(it) },
            confirmButton = {
                Button(onClick = viewModel::clearMessages) {
                    Text("OK")
                }
            }
        )
    }

    state.successMessage?.let {
        AlertDialog(
            onDismissRequest = viewModel::clearMessages,
            title = { Text("Compra exitosa") },
            text = { Text(it) },
            confirmButton = {
                Button(onClick = viewModel::clearMessages) {
                    Text("OK")
                }
            }
        )
    }
}
