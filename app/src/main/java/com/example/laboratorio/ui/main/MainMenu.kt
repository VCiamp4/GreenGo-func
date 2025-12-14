package com.example.laboratorio.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainMenu(
    onLogout: () -> Unit,
    viewModel: MainMenuViewModel = viewModel()
) {
    val state = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.loadUserData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator()
            }

            state.username != null -> {
                Text(text = "Usuario: ${state.username}")
                Text(text = "ID: ${state.userId}")
            }

            else -> {
                Text(text = state.errorMessage ?: "Sin datos")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onLogout) {
            Text("Cerrar sesión")
        }
    }
}
