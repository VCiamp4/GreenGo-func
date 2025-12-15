package com.example.laboratorio.ui.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Email
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
fun SignUpScreen(
    onBackToLogin: () -> Unit,
    onSignUpSuccess: (email: String) -> Unit,
    viewModel: SignUpViewModel = viewModel()
) {
    val state = viewModel.uiState

    Box(modifier = Modifier.fillMaxSize()) {

        // Fondo con gradiente (SIN imagen)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
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

            // Logo
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(GreenPrimary, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Autorenew,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Crear Cuenta",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Regístrate para empezar a reciclar",
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

                    Text("Registro", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "Completa tus datos para crear una cuenta",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = state.username,
                        onValueChange = viewModel::onUsernameChange,
                        placeholder = { Text("Nombre de usuario") },
                        leadingIcon = { Icon(Icons.Filled.Person, null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LightGrayBg, RoundedCornerShape(12.dp)),
                        singleLine = true
                    )

                    Spacer(Modifier.height(12.dp))

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.password,
                        onValueChange = viewModel::onPasswordChange,
                        placeholder = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Filled.Lock, null) },
                        trailingIcon = {
                            IconButton(onClick = viewModel::toggleShowPassword) {
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
                        singleLine = true
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.confirmPassword,
                        onValueChange = viewModel::onConfirmPasswordChange,
                        placeholder = { Text("Confirmar contraseña") },
                        leadingIcon = { Icon(Icons.Filled.Lock, null) },
                        trailingIcon = {
                            IconButton(onClick = viewModel::toggleShowConfirmPassword) {
                                Icon(
                                    imageVector = if (state.showConfirmPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = null
                                )
                            }
                        },
                        visualTransformation =
                            if (state.showConfirmPassword) VisualTransformation.None
                            else PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LightGrayBg, RoundedCornerShape(12.dp)),
                        singleLine = true
                    )

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.signup { email -> onSignUpSuccess(email) } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        shape = RoundedCornerShape(14.dp),
                        enabled = !state.isLoading
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                color = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Creando cuenta...", color = Color.White)
                        } else {
                            Text("Crear Cuenta", color = Color.White)
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    state.errorMessage?.let { msg ->
                        Text(
                            text = msg,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(Modifier.height(6.dp))
                    }

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("¿Ya tienes cuenta? ")
                        Text(
                            text = "Inicia sesión",
                            color = GreenPrimary,
                            modifier = Modifier.clickable(onClick = onBackToLogin)
                        )
                    }
                }
            }
        }
    }
}
