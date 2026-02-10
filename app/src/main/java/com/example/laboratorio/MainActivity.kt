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

        // Configuración de OSMDroid para el mapa
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
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

// Enum simplificado: Solo las pantallas que NO están dentro del BottomBar del menú
enum class AppScreen {
    LOGIN, SIGNUP, MENU, ACHIEVEMENTS
}

@Composable
fun AppEntry() {
    // Controlamos la navegación principal
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.LOGIN) }

    // Variables para mantener sesión básica en UI
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }

    when (currentScreen) {
        // 1. PANTALLA DE LOGIN
        AppScreen.LOGIN -> {
            LoginScreen(
                onLoginSuccess = { name, email ->
                    isLoggedIn = true
                    currentScreen = AppScreen.MENU
                },
                onGoToSignUp = {
                    currentScreen = AppScreen.SIGNUP
                }
            )
        }

        // 2. PANTALLA DE REGISTRO
        AppScreen.SIGNUP -> {
            SignUpScreen(
                onBackToLogin = { currentScreen = AppScreen.LOGIN },
                onSignUpSuccess = { email ->
                    currentScreen = AppScreen.LOGIN
                }
            )
        }

        // 3. MENÚ PRINCIPAL
        // Nota: Tu MainMenu ya se encarga de mostrar el Mapa, Escáner, Tienda y Ranking internamente.
        AppScreen.MENU -> {
            MainMenu(
                onLogout = {
                    TokenStore.clear()
                    isLoggedIn = false
                    currentScreen = AppScreen.LOGIN
                },
                onAchievementsClick = {
                    currentScreen = AppScreen.ACHIEVEMENTS
                }
            )
        }

        // 4. PANTALLA DE LOGROS
        AppScreen.ACHIEVEMENTS -> {
            AchievementsScreen(
                onBack = { currentScreen = AppScreen.MENU }
            )
        }
    }
}