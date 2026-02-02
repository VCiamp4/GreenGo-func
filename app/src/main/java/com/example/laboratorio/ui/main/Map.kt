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
import com.example.laboratorio.ui.main.MainMenuViewModel
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@Composable
fun MapaOsmContent(estaciones: List<Estacion>, viewModel: MainMenuViewModel) {
    val context = LocalContext.current
    val state = viewModel.uiState

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
                this.onResume()
            }
        },
        modifier = Modifier.fillMaxSize(),
        update = { mapView ->

            mapView.overlays.clear()

            val myLocationOverlay = MyLocationNewOverlay(mapView).apply {
                enableMyLocation()
            }

            myLocationOverlay.runOnFirstFix {
                mapView.post {
                    mapView.controller.animateTo(myLocationOverlay.myLocation)
                    mapView.invalidate()
                }
            }
            mapView.overlays.add(myLocationOverlay)

            state.roadOverlay?.let {
                mapView.overlays.add(it)
            }

            estaciones.forEach { estacion ->
                val marker = Marker(mapView)
                marker.position = GeoPoint(estacion.latitud, estacion.longitud)
                marker.title = estacion.nombre
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                marker.setOnMarkerClickListener { m, _ ->
                    val userLoc = myLocationOverlay.myLocation
                    if (userLoc != null) {
                        viewModel.calcularRuta(context, userLoc, m.position)
                    }
                    m.showInfoWindow()
                    true
                }
                mapView.overlays.add(marker)
            }
            mapView.invalidate()
        },
        onRelease = { mapView ->
            mapView.onPause()
        }
    )
}