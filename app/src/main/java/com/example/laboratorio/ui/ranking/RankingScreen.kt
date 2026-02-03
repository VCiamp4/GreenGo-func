package com.example.laboratorio.ui.ranking

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RankingScreen(
    onBack: () -> Unit,
    viewModel: RankingViewModel = viewModel()
) {
    val state = viewModel.uiState

    LaunchedEffect(
        state.period,
        state.mode,
        state.selectedResidue
    ) {
        Log.d(
            "RANKING_EFFECT",
            "Effect triggered: ${state.period}, ${state.mode}, ${state.selectedResidue}"
        )
        viewModel.loadRanking()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F7F6))
    ) {

        /* ---------- HEADER ---------- */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
            Spacer(Modifier.width(8.dp))
            Column {
                Text(
                    text = "Ranking",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Top recicladores",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }

        /* ---------- TABS GLOBAL / SEMANAL ---------- */
        TabRow(selectedTabIndex = state.period.ordinal) {
            RankingPeriod.values().forEach { period ->
                Tab(
                    selected = state.period == period,
                    onClick = { viewModel.setPeriod(period) },
                    text = { Text(period.label) }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        /* ---------- FILTRO ---------- */
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = state.mode == RankingMode.PUNTOS,
                onClick = { viewModel.setMode(RankingMode.PUNTOS) },
                label = { Text("Puntos") }
            )
            FilterChip(
                selected = state.mode == RankingMode.RESIDUO,
                onClick = { viewModel.setMode(RankingMode.RESIDUO) },
                label = { Text("Por residuo") }
            )
        }

        Spacer(Modifier.height(12.dp))

        /* ---------- LISTA ---------- */
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(state.errorMessage, color = Color.Red)
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(state.items) { index, user ->
                        RankingItem(
                            position = index + 1,
                            username = user.username,
                            points = user.totalPuntos
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RankingItem(
    position: Int,
    username: String,
    points: Int
) {
    val bgColor = when (position) {
        1 -> Color(0xFFFFF3CD)
        2 -> Color(0xFFE5E7EB)
        3 -> Color(0xFFFFEDD5)
        else -> Color.White
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$position",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(32.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(username, fontWeight = FontWeight.SemiBold)
                Text("$points pts", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
