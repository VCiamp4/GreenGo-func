package com.example.laboratorio.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laboratorio.ui.theme.GreenDark
import com.example.laboratorio.ui.theme.GreenPrimary
import com.example.laboratorio.ui.theme.GreenTeal
import com.example.laboratorio.ui.theme.LightGrayBg

@Composable
fun LoginScreen(
    onLoginSuccess: (String, String) -> Unit,
    onGoToSignUp: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val state = viewModel.uiState

    Box(modifier = Modifier.fillMaxSize()) {

        // Fondo simple con gradiente (sin imagen)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            GreenDark.copy(alpha = 0.95f),
                            GreenTeal.copy(alpha = 0.90f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))

            // Logo simple
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(GreenPrimary, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("GG", color = Color.White, style = MaterialTheme.typography.titleLarge)
            }

            Spacer(Modifier.height(16.dp))

            Text("Bienvenido", color = Color.White, style = MaterialTheme.typography.headlineSmall)
            Text(
                "Inicia sesión para continuar reciclando",
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(28.dp))

            Card(
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Text("Iniciar Sesión", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Ingresa tus credenciales para acceder",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                    Spacer(Modifier.height(16.dp))

                    // Usuario
                    OutlinedTextField(
                        value = state.email, // lo mantenemos así para no tocar ViewModel ahora
                        onValueChange = viewModel::onEmailChange,
                        label = { Text("Usuario") },
                        placeholder = { Text("tu_usuario") },
                        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LightGrayBg, RoundedCornerShape(12.dp)),
                        singleLine = true,
                        enabled = !state.isLoading
                    )

                    Spacer(Modifier.height(12.dp))

                    // Contraseña
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = viewModel::onPasswordChange,
                        label = { Text("Contraseña") },
                        placeholder = { Text("••••••••") },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                        trailingIcon = {
                            IconButton(
                                onClick = viewModel::toggleShowPassword,
                                enabled = !state.isLoading
                            ) {
                                Icon(
                                    imageVector = if (state.showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = null
                                )
                            }
                        },
                        visualTransformation =
                            if (state.showPassword) VisualTransformation.None
                            else PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LightGrayBg, RoundedCornerShape(12.dp)),
                        singleLine = true,
                        enabled = !state.isLoading
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "¿Olvidaste tu contraseña?",
                        color = GreenPrimary,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable(enabled = !state.isLoading) {
                                // TODO: recuperar contraseña (si lo piden)
                            }
                    )

                    Spacer(Modifier.height(16.dp))

                    // Botón login
                    Button(
                        onClick = {
                            viewModel.login { name, email ->
                                onLoginSuccess(name, email)
                            }
                        },
                        enabled = !state.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                color = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text("Iniciando sesión...", color = Color.White)
                        } else {
                            Text("Iniciar Sesión", color = Color.White)
                        }
                    }

                    // Error visible
                    state.errorMessage?.let { msg ->
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = msg,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // Registro
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("¿No tienes cuenta? ")
                        Text(
                            "Regístrate",
                            color = GreenPrimary,
                            modifier = Modifier.clickable(enabled = !state.isLoading) {
                                onGoToSignUp()
                            }
                        )
                    }
                }
            }
        }
    }
}
