package com.example.laboratorio

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.laboratorio.ui.auth.network.TokenStore
import com.example.laboratorio.ui.login.LoginScreen
import com.example.laboratorio.ui.main.MainMenu
import com.example.laboratorio.ui.theme.LaboratorioTheme
import com.example.laboratorio.ui.signup.SignUpScreen
import org.osmdroid.config.Configuration
import com.example.laboratorio.ui.achievements.AchievementsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        // El userAgent debe ser el ID de tu app
        Configuration.getInstance().userAgentValue = packageName

        checkLocationPermissions()

        setContent {
            LaboratorioTheme {
                AppEntry()
            }
        }
    }

    private fun checkLocationPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val notGranted = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (notGranted.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, notGranted.toTypedArray(), 0)
        }
    }

}



@Composable
fun AppEntry() {
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }
    var userName by rememberSaveable { mutableStateOf("") }
    var userEmail by rememberSaveable { mutableStateOf("") }
    var showSignUp by rememberSaveable { mutableStateOf(false) }

    // 1. NUEVO ESTADO: Controla si se ve la pantalla de logros
    var showAchievements by rememberSaveable { mutableStateOf(false) }

    when {
        // 2. Si showAchievements es true, mostramos esa pantalla
        showAchievements -> {
            AchievementsScreen(
                onBack = { showAchievements = false } // Al volver, ocultamos logros y regresa al MainMenu
            )
        }

        isLoggedIn -> {
            MainMenu(
                onLogout = {
                    TokenStore.clear()
                    isLoggedIn = false
                },
                // 3. Pasamos la acción para activar el estado de logros
                onAchievementsClick = {
                    showAchievements = true
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
