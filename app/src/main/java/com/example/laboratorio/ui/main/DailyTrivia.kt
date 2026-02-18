package com.example.laboratorio.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiObjects
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laboratorio.data.UserRepository

// --- VIEWMODEL ---
data class TriviaQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val reward: Int = 100
)

class DailyTriviaViewModel : ViewModel() {
    var isDialogVisible by mutableStateOf(false)
    var hasPlayedToday by mutableStateOf(false)
    var selectedOptionIndex by mutableStateOf<Int?>(null)
    var isAnswerCorrect by mutableStateOf<Boolean?>(null)

    // NUEVO: Estado para saber si usó la pista
    var isHintUsed by mutableStateOf(false)
        private set

    // Pregunta
    val currentQuestion = TriviaQuestion(
        question = "¿Cuánto tarda en degradarse una botella de plástico?",
        options = listOf("50 años", "100 años", "500 años", "Nunca"),
        correctIndex = 2
    )

    fun openTrivia() {
        if (!hasPlayedToday) isDialogVisible = true
    }

    fun closeDialog() {
        isDialogVisible = false
    }

    fun selectOption(index: Int) {
        if (!hasPlayedToday) selectedOptionIndex = index
    }

    // NUEVO: Función para usar la pista
    fun useHint() {
        // Solo si no la usó y tiene saldo
        if (!isHintUsed && UserRepository.triviaHints.value > 0) {
            UserRepository.consumeHint()
            isHintUsed = true
        }
    }

    fun submitAnswer() {
        val selected = selectedOptionIndex ?: return

        val correct = selected == currentQuestion.correctIndex
        isAnswerCorrect = correct
        hasPlayedToday = true

        if (correct) {
            // LÓGICA DE PENALIZACIÓN:
            // Si usó pista, gana la mitad (50%). Si no, el 100%.
            val basePoints = if (isHintUsed) currentQuestion.reward / 2 else currentQuestion.reward

            // El repositorio se encarga de aplicar multiplicadores (boosters) si los hay
            UserRepository.addPoints(basePoints)
        }
    }
}

// --- TARJETA DE ACCESO (HOME) ---
@Composable
fun DailyTriviaCard(viewModel: DailyTriviaViewModel = viewModel()) {
    val isCompleted = viewModel.hasPlayedToday
    val GreenPrimary = Color(0xFF00C49A)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { if (!isCompleted) viewModel.openTrivia() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = if(isCompleted) Color(0xFFE8F5E9) else Color(0xFFFFF8E7)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(if (isCompleted) GreenPrimary else Color(0xFFFFC107), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(if (isCompleted) Icons.Default.Check else Icons.Default.Lightbulb, null, tint = Color.White)
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(if (isCompleted) "Desafío completado" else "Desafío Diario", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(if (isCompleted) "¡Vuelve mañana!" else "Gana +${viewModel.currentQuestion.reward} pts", color = if (isCompleted) GreenPrimary else Color(0xFFFF8F00), fontSize = 14.sp)
            }
            if (!isCompleted) {
                Button(onClick = { viewModel.openTrivia() }, colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary), shape = RoundedCornerShape(12.dp), modifier = Modifier.height(36.dp), contentPadding = PaddingValues(horizontal = 16.dp)) {
                    Text("Jugar", fontSize = 12.sp)
                }
            }
        }
    }

    if (viewModel.isDialogVisible) {
        TriviaDialogUI(viewModel)
    }
}

// --- DIALOGO DEL JUEGO ---
@Composable
fun TriviaDialogUI(viewModel: DailyTriviaViewModel) {
    val hintsAvailable by UserRepository.triviaHints.collectAsState()

    // Leemos el estado del VM en lugar de una variable local
    val hintUsed = viewModel.isHintUsed

    Dialog(onDismissRequest = { viewModel.closeDialog() }) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.EmojiObjects, null, tint = Color(0xFFFFC107), modifier = Modifier.size(48.dp))
                Spacer(Modifier.height(16.dp))
                Text("Pregunta del Día", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                Spacer(Modifier.height(8.dp))

                Text(
                    viewModel.currentQuestion.question,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                // Botón de Pista
                if (!viewModel.hasPlayedToday && !hintUsed && hintsAvailable > 0) {
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { viewModel.useHint() }, // Llamamos al VM
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF6F00))
                    ) {
                        Icon(Icons.Default.Lightbulb, null, modifier = Modifier.size(16.dp))
                        // Avisamos que reduce la recompensa
                        Text(" Usar Pista (Recompensa -50%)")
                    }
                } else if (hintUsed) {
                    Spacer(Modifier.height(8.dp))
                    Text("Pista activada: -50% Puntos", color = Color(0xFFFF6F00), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(24.dp))

                // Opciones
                viewModel.currentQuestion.options.forEachIndexed { index, option ->
                    val isSelected = viewModel.selectedOptionIndex == index
                    val isCorrectAnswer = index == viewModel.currentQuestion.correctIndex
                    val showResult = viewModel.hasPlayedToday

                    val bgColor = when {
                        showResult && isCorrectAnswer -> Color(0xFF00C49A)
                        showResult && isSelected && !isCorrectAnswer -> Color(0xFFEF5350)
                        hintUsed && isCorrectAnswer -> Color(0xFFFFF59D)
                        isSelected -> Color(0xFF00C49A)
                        else -> Color(0xFFF5F5F5)
                    }

                    val contentColor = if (isSelected || (showResult && isCorrectAnswer) || (showResult && isSelected)) Color.White else Color.Black

                    Button(
                        onClick = { viewModel.selectOption(index) },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = bgColor),
                        enabled = !showResult,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(option, color = contentColor, modifier = Modifier.weight(1f))
                            if (showResult) {
                                if (isCorrectAnswer) Icon(Icons.Default.Check, null, tint = Color.White)
                                else if (isSelected) Icon(Icons.Default.Close, null, tint = Color.White)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                if (!viewModel.hasPlayedToday) {
                    Button(
                        onClick = { viewModel.submitAnswer() },
                        enabled = viewModel.selectedOptionIndex != null,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text("Responder")
                    }
                } else {
                    // RESULTADO
                    val multiplier = UserRepository.pointMultiplier.collectAsState().value

                    // Calculamos visualmente lo mismo que en la lógica:
                    val basePoints = if (hintUsed) viewModel.currentQuestion.reward / 2 else viewModel.currentQuestion.reward
                    val totalWon = basePoints * multiplier

                    if (viewModel.isAnswerCorrect == true) {
                        Text("+$totalWon Puntos!", color = Color(0xFF00C49A), fontWeight = FontWeight.Bold, fontSize = 20.sp)

                        // Mensajes extra
                        if (hintUsed) {
                            Text("(Puntos reducidos por ayuda)", color = Color.Gray, fontSize = 12.sp)
                        }
                        if (multiplier > 1) {
                            Text("¡Booster x$multiplier!", color = Color(0xFFFF9800), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                    } else {
                        Text("¡Incorrecto!", color = Color(0xFFEF5350), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("La correcta era: ${viewModel.currentQuestion.options[viewModel.currentQuestion.correctIndex]}", fontSize = 12.sp, color = Color.Gray)
                    }

                    Spacer(Modifier.height(12.dp))
                    Button(onClick = { viewModel.closeDialog() }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)) {
                        Text("Cerrar", color = Color.Black)
                    }
                }
            }
        }
    }
}