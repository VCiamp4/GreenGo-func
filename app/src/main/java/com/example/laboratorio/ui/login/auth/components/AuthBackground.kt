package com.example.laboratorio.ui.auth.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun AuthBackground(
    backgroundResId: Int,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // Imagen de fondo (poné la tuya en res/drawable)
        Image(
            painter = painterResource(id = backgroundResId),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Overlay con gradiente (simula tu overlay de React)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xE6143B2F), // verde oscuro con alpha
                            Color(0xD6166B4F),
                            Color(0xE6124B4A)
                        )
                    )
                )
        )

        // Contenido arriba
        content()
    }
}
