package com.example.laboratorio.ui.main

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun MainMenu(
    onLogout: () -> Unit,
    viewModel: MainMenuViewModel = viewModel()
) {
    val state = viewModel.uiState

    val scanLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        viewModel.onScanResult(result.contents)
    }

    LaunchedEffect(Unit) {
        viewModel.loadUserData()
    }

    if (state.reclamarMessage != null || state.reclamarError != null) {
        val isError = state.reclamarError != null
        AlertDialog(
            onDismissRequest = { viewModel.clearReclamarStatus() },
            title = {
                Text(
                    text = if (isError) "Error" else "Residuo Reclamado",
                    color = if (isError) Color.Red else Color.Green
                )
            },
            text = {
                Column {
                    if (isError) {
                        state.reclamarError?.let { Text(it) }
                    } else {
                        state.reclamarMessage?.let { Text(it) }
                        state.categoria?.let { Text("Categoría: $it") }
                        state.puntos?.let { Text("Puntos: $it") }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.clearReclamarStatus() },
                    colors = ButtonDefaults.buttonColors(containerColor = if (isError) Color.Red else Color.Green)
                ) {
                    Text("Cerrar")
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
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

            Button(onClick = {
                val options = ScanOptions()
                options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                options.setPrompt("Escanea un código QR")
                options.setCameraId(0)  // Usa una cámara específica del dispositivo
                options.setBeepEnabled(false)
                options.setBarcodeImageEnabled(true)
                scanLauncher.launch(options)
            }) {
                Text("Escanear QR")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = onLogout) {
                Text("Cerrar sesión")
            }
        }
        if (state.isClaiming) {
            CircularProgressIndicator()
        }
    }
}
