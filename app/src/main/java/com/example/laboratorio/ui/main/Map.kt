package com.example.laboratorio.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.example.laboratorio.ui.network.Estacion
import org.osmdroid.views.MapView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import org.osmdroid.views.overlay.Marker


@Composable
fun MapaOsmContent(estaciones: List<Estacion>) {
    val context = LocalContext.current

    AndroidView(
        factory = { context ->
            MapView(context).apply {
                id = View.generateViewId()
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)

                val controller = this.controller
                controller.setZoom(16.5)

                val startPoint = GeoPoint(-34.913, -57.9495)
                controller.setCenter(startPoint)
            }
        },
        modifier = Modifier.fillMaxSize(),
        update = { mapView ->
            mapView.overlays.clear()

            estaciones.forEach { estacion ->
                val marker = Marker(mapView)
                marker.position = GeoPoint(estacion.latitud, estacion.longitud)
                marker.title = estacion.nombre
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                mapView.overlays.add(marker)
            }
            mapView.invalidate()
        }
    )
}