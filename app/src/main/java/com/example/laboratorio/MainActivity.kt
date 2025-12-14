package com.example.laboratorio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.laboratorio.ui.auth.network.TokenStore
import com.example.laboratorio.ui.login.LoginScreen
import com.example.laboratorio.ui.main.MainMenu
import com.example.laboratorio.ui.theme.LaboratorioTheme
import com.example.laboratorio.ui.signup.SignUpScreen

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
            MainMenu(
                onLogout = {
                    TokenStore.clear()
                    isLoggedIn = false
                }
            )
        }


        showSignUp -> {
            SignUpScreen(
                onBackToLogin = { showSignUp = false },
                onSignUpSuccess = { email ->
                    userEmail = email
                    showSignUp = false
                }
            )
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
