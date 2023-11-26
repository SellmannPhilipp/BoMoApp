package com.praktikum.bomoapp.activities

import android.preference.PreferenceManager
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
            if(LastLocationViewModel.geoPoint.isNotNull()) {
                LastLocationViewModel.geoPoint?.let { addMarkerToMap(mapView, it, "Position") }
            }

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

            if(MeasurementViewModel.showUserTrackedMeasuringPoints) {
                for (point in MeasurementViewModel.userTrackedMeasuringPoints) {
                    addMarkerToMap(mapView, point.getLocation(), "Eigener Messpunkt")
                }
            }

            if(MeasurementViewModel.showAllTrackedMeasuringPoints) {
                for (point in MeasurementViewModel.generalTrackedMeasuringPoints) {
                    addMarkerToMap(mapView, point.getLocation(), "Automatischer Messpunkt")
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

fun addMarkerToMap(view: MapView, geoPoint: GeoPoint, name: String) {
    val mapView = view

    val marker = Marker(mapView)
    marker.position = geoPoint
    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
    marker.title = name

    mapView.overlays.add(marker)

    mapView.invalidate()
}

fun showPathOnMap(view: MapView, polylinePoints: List<GeoPoint>) {
    val mapView = view

    val polyline = Polyline()
    polyline.setPoints(polylinePoints)
    polyline.color = 0x990000FF.toInt() // Farbe der Linie
    polyline.width = 6f // Breite der Linie

    mapView.overlayManager.add(polyline)

    // Marker für jeden GeoPoint hinzufügen
    for (point in polylinePoints) {
        val marker = Marker(mapView)
        marker.position = point
        marker.title =
            "Vorgabe\n" + "Latitude: " + point.latitude + "\n" + "Longitude: " + point.longitude
        mapView.overlays.add(marker)
    }

    mapView.invalidate()
}