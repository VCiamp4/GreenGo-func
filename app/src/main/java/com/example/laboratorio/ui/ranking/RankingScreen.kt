package com.example.laboratorio.ui.main.ranking

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RankingScreen(
    semanal: Boolean,
    tipoResiduo: String? = null,
    viewModel: RankingViewModel = viewModel()
) {
    val state = viewModel.uiState.value

    LaunchedEffect(semanal, tipoResiduo) {
        viewModel.loadRanking(semanal, tipoResiduo)
    }

    when {
        state.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.errorMessage != null -> {
            Text(
                text = state.errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        else -> {
            Column(modifier = Modifier.padding(16.dp)) {
                state.entries.forEachIndexed { index, entry ->
                    Text(
                        text = "${index + 1}. ${entry.username} — ${entry.puntos} pts"
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}
