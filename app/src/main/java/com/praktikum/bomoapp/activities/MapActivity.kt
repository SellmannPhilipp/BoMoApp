package com.praktikum.bomoapp.activities

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.preference.PreferenceManager
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import co.yml.charts.common.extensions.isNotNull
import com.praktikum.bomoapp.PathController
import com.praktikum.bomoapp.viewmodels.LastLocationViewModel
import com.praktikum.bomoapp.viewmodels.MeasurementViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline


@Composable
fun OsmdroidMapView() {
    val context = LocalContext.current
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val mapView = MapView(context)
            Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))
            Configuration.getInstance().userAgentValue = "BoMoApp"
            setMapCamera(mapView, GeoPoint(51.4818, 7.2162), 15)
            mapView.setTileSource(TileSourceFactory.MAPNIK)
            mapView.setMultiTouchControls(true)
            /*
            if(LastLocationViewModel.geoPoint.isNotNull()) {
                LastLocationViewModel.geoPoint?.let { addMarkerToMap(mapView, it, "Position") }
            } */

            //Route 1
            val polylinePointsOne = listOf(
                GeoPoint(51.48122, 7.22009),
                GeoPoint(51.48435, 7.23187),
                GeoPoint(51.49931, 7.19633),
            )

            //Route 2
            val polylinePointsTwo = listOf(
                GeoPoint(51.48122, 7.22009),
                GeoPoint(51.48435, 7.23187),
            )

            if(PathController.getPathToShow() == 1) {
                showPathOnMap(mapView, polylinePointsOne)
            } else if(PathController.getPathToShow() == 2) {
                showPathOnMap(mapView, polylinePointsTwo)
            }

            addMarkerToMap(mapView, GeoPoint(52.00, 7.00), "Test", Color.BLUE)
            addMarkerToMap(mapView, GeoPoint(52.00, 7.00), "Test", Color.GREEN)

            if(MeasurementViewModel.showTrackedMeasuringPoints) {
                for (point in MeasurementViewModel.generalTrackedMeasuringPoints) {
                    addMarkerToMap(mapView, point.getLocation(), "Allgemeiner Messpunkt", Color.BLUE)
                }

                for (point in MeasurementViewModel.userTrackedMeasuringPoints) {
                    addMarkerToMap(mapView, point.getLocation(), "Eigener Messpunkt", Color.GREEN)
                }
            }

            mapView
        }
    )
}

fun setMapCamera(view: MapView, geoPoint: GeoPoint, zoomLevel: Int) {
    val mapView = view
    val mapController = mapView.controller
    mapController.setCenter(geoPoint)
    mapController.setZoom(zoomLevel)
}

fun addMarkerToMap(view: MapView, geoPoint: GeoPoint, title: String, color: Int) {
    val mapView = view

    val oval = ShapeDrawable(OvalShape()).apply {
        intrinsicHeight = 30 // Hier die gewünschte Höhe des Punkts einstellen
        intrinsicWidth = 30 // Hier die gewünschte Breite des Punkts einstellen
        paint.color = color
    }

    val marker = Marker(mapView)
    marker.position = geoPoint
    marker.icon = oval
    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
    marker.title = title

    mapView.overlays.add(marker)

    mapView.invalidate()
}

fun showPathOnMap(view: MapView, polylinePoints: List<GeoPoint>) {
    val mapView = view

    val polyline = Polyline()
    polyline.setPoints(polylinePoints)
    polyline.color = Color.RED // Farbe der Linie
    polyline.width = 6f // Breite der Linie

    mapView.overlayManager.add(polyline)

    // Marker für jeden GeoPoint hinzufügen
    for (point in polylinePoints) {
        addMarkerToMap(mapView, point, "Route", Color.RED)
    }

    mapView.invalidate()
}