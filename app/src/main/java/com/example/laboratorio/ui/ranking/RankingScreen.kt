package com.example.laboratorio.ui.ranking

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

private val residues = listOf(
    "Plástico",
    "Vidrio",
    "Papel",
    "Metal",
    "Orgánico"
)

@Composable
fun RankingScreen(
    viewModel: RankingViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        Text(
            text = "Ranking",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        // Tabs Global / Semanal
        TabRow(
            selectedTabIndex = state.period.ordinal,
            containerColor = Color.White,
            contentColor = Color(0xFF0F8C6E)
        ) {
            RankingPeriod.values().forEach { period ->
                Tab(
                    selected = state.period == period,
                    onClick = { viewModel.changePeriod(period) },
                    text = {
                        Text(if (period == RankingPeriod.GLOBAL) "Global" else "Semanal")
                    }
                )
            }
        }

        // Chips de modo
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = state.mode == RankingMode.TOTAL_POINTS,
                onClick = { viewModel.changeMode(RankingMode.TOTAL_POINTS) },
                label = { Text("Puntos") }
            )
            FilterChip(
                selected = state.mode == RankingMode.BY_RESIDUE,
                onClick = { viewModel.changeMode(RankingMode.BY_RESIDUE) },
                label = { Text("Por residuo") }
            )
        }

        // Selector de residuo
        if (state.mode == RankingMode.BY_RESIDUE) {
            var expanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = state.selectedResidue,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Residuo") },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    residues.forEach { residue ->
                        DropdownMenuItem(
                            text = { Text(residue) },
                            onClick = {
                                expanded = false
                                viewModel.changeResidue(residue)
                            }
                        )
                    }
                }
            }
        }

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }

            else -> {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    state.ranking.forEach { entry ->
                        RankingRow(entry)
                    }
                }
            }
        }
    }
}

@Composable
private fun RankingRow(entry: RankingEntry) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#${entry.position}",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(40.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(entry.username, fontWeight = FontWeight.SemiBold)
                entry.residueType?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Text(
                text = "${entry.points} pts",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F8C6E)
            )
        }
    }
}
