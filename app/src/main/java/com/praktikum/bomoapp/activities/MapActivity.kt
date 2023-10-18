package com.praktikum.bomoapp
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import android.Manifest
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

class MapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), 0)
        }
        super.onCreate(savedInstanceState)
        setContent {
            OsmdroidMapView()
        }
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        Configuration.getInstance().userAgentValue = "BoMoApp"

    }
}

@Composable
fun OsmdroidMapView() {
    val context = LocalContext.current
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val mapView = MapView(context)
            setMapCamera(mapView, 51.4818, 7.2162, 20)
            mapView.setTileSource(TileSourceFactory.MAPNIK)
            mapView.setBuiltInZoomControls(true)
            mapView.setMultiTouchControls(true)
            mapView
        }
    )
}

fun setMapCamera(view: MapView, latitude: Double, longitude: Double, zoomLevel: Int) {
    val mapView = view
    val mapController = mapView.controller
    val targetLatitude = latitude
    val targetLongitude = longitude
    val targetZoomLevel = zoomLevel
    mapController.setCenter(GeoPoint(targetLatitude, targetLongitude))
    mapController.setZoom(zoomLevel)
}

fun addMarkerToMap(view: MapView, latitude: Double, longitude: Double, name: String) {
    val mapView = view
    val geoPoint = GeoPoint(latitude, longitude)

    val marker = Marker(mapView)
    marker.position = geoPoint
    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
    marker.title = name

    mapView.overlays.add(marker)

    mapView.invalidate()
}