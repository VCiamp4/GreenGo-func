package com.example.laboratorio.ui.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RankingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        RankingUiState(isLoading = true)
    )
    val uiState: StateFlow<RankingUiState> = _uiState

    init {
        reload()
    }

    fun changePeriod(period: RankingPeriod) {
        _uiState.update { it.copy(period = period, isLoading = true) }
        reload()
    }

    fun changeMode(mode: RankingMode) {
        _uiState.update { it.copy(mode = mode, isLoading = true) }
        reload()
    }

    fun changeResidue(residue: String) {
        _uiState.update { it.copy(selectedResidue = residue, isLoading = true) }
        reload()
    }

    private fun reload() {
        viewModelScope.launch {
            delay(600)

            val state = _uiState.value

            val data = when (state.mode) {
                RankingMode.TOTAL_POINTS ->
                    mockTotal(state.period)

                RankingMode.BY_RESIDUE ->
                    mockByResidue(state.period, state.selectedResidue)
            }

            _uiState.update {
                it.copy(
                    ranking = data,
                    isLoading = false
                )
            }
        }
    }

    private fun mockTotal(period: RankingPeriod): List<RankingEntry> =
        (1..10).map {
            RankingEntry(
                position = it,
                username = "Usuario$it",
                points = if (period == RankingPeriod.GLOBAL)
                    2000 - it * 120
                else
                    450 - it * 35
            )
        }

    private fun mockByResidue(
        period: RankingPeriod,
        residue: String
    ): List<RankingEntry> =
        (1..10).map {
            RankingEntry(
                position = it,
                username = "$residue-$it",
                points = if (period == RankingPeriod.GLOBAL)
                    900 - it * 40
                else
                    220 - it * 18,
                residueType = residue
            )
        }
}
