package com.example.greengo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.greengo.ui.login.LoginScreen
import com.example.laboratorio.ui.theme.LaboratorioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaboratorioTheme {
                AppEntry()
            }
        }
    }
}

@Composable
fun AppEntry() {
    var isLoggedIn by remember { mutableStateOf(false) }
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var showSignUp by remember { mutableStateOf(false) }

    when {
        isLoggedIn -> {
            // TODO: acá va tu MainMenu cuando lo tengamos
            // Por ahora mostramos un texto de prueba:
            androidx.compose.material3.Text(text = "Hola $userName ($userEmail)")
        }

        showSignUp -> {
            // TODO: acá va tu pantalla de registro
            androidx.compose.material3.Text(text = "Pantalla de Registro (TODO)")
        }

        else -> {
            LoginScreen(
                onLoginSuccess = { name, email ->
                    userName = name
                    userEmail = email
                    isLoggedIn = true
                },
                onGoToSignUp = {
                    showSignUp = true
                }
            )
        }
    }
}
