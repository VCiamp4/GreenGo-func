package com.example.laboratorio.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.ViewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * ViewModel falso para controlar el estado de la UI directamente en los tests.
 */
class FakeMainMenuViewModel : MainMenuViewModel() {
    // Sobrescribimos el uiState para poder controlarlo desde el test
    override var uiState by mutableStateOf(MainMenuUiState())

    fun setState(newState: MainMenuUiState) {
        uiState = newState
    }

    // Sobrescribimos los métodos que no queremos que hagan nada en este test
    override fun onScanResult(result: String?) { /* No-op */ }
    override fun loadUserData() { /* No-op */ }
    override fun clearReclamarStatus() { /* No-op */ }
}

@RunWith(AndroidJUnit4::class)
class MainMenuTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeViewModel = FakeMainMenuViewModel()

    @Test
    fun successfulScan_updatesTotalPointsCorrectly() {
        // 1. Arrange: Estado inicial con 100 puntos
        val initialState = MainMenuUiState(
            username = "Test User",
            puntosTotales = 100
        )
        fakeViewModel.setState(initialState)

        // 2. Act: Cargar la UI con el estado inicial
        composeTestRule.setContent {
            MainMenu(
                onLogout = {}, // No necesitamos hacer nada en el logout para este test
                viewModel = fakeViewModel
            )
        }

        // 3. Assert: Verificar que el puntaje inicial se muestra correctamente
        composeTestRule.onNodeWithText("Puntos Totales: 100").assertIsDisplayed()

        // 4. Arrange: Nuevo estado después de un escaneo exitoso que da 50 puntos
        val pointsFromScan = 50
        val updatedState = initialState.copy(
            puntosTotales = initialState.puntosTotales + pointsFromScan
        )
        
        // 5. Act: Simular la actualización del estado que ocurriría después de un escaneo
        fakeViewModel.setState(updatedState)

        // 6. Assert: Verificar que el nuevo puntaje se muestra en la UI
        composeTestRule.onNodeWithText("Puntos Totales: 150").assertIsDisplayed()
        // Verificamos que el texto antiguo ya no está
        composeTestRule.onNodeWithText("Puntos Totales: 100").assertDoesNotExist()
    }
}
