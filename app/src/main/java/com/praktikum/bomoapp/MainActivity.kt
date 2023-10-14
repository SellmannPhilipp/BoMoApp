package com.praktikum.bomoapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.praktikum.bomoapp.ui.theme.BoMoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if(ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                GpsTracking()
            }
            else {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),0)
            }
        }
    }
}

//Function to track location by network provider
@SuppressLint("MissingPermission")
@Composable
fun NetworkTracking() {
    val ctx = LocalContext.current
    val locationManager: LocationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    var latitude by remember { mutableStateOf(0.0) }
    var longitude by remember { mutableStateOf(0.0) }

    var tracking by remember { mutableStateOf(false) }

    var btnTextTrackingEnabled = if (tracking) "Ein" else "Aus"

    val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            latitude = location.latitude
            longitude = location.longitude
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { tracking = !tracking }) {
            Text(text = "Network-Tracking: $btnTextTrackingEnabled")
        }
    }

    DisposableEffect(tracking) {
        if (tracking) {
            Log.d("Debug", "Tracking wird gestartet")
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 0f, locationListener)
            Log.d("Debug", "Longitiude: $longitude\nLatitude: $latitude")
        } else {
            Log.d("Debug", "Tracking wird gestoppt")
            locationManager.removeUpdates(locationListener)
        }

        onDispose {
            // Cleanup, if necessary
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun GpsTracking() {
    val ctx = LocalContext.current
    val locationManager: LocationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    var latitude by remember { mutableStateOf(0.0) }
    var longitude by remember { mutableStateOf(0.0) }

    var tracking by remember { mutableStateOf(false) }

    var btnTextTrackingEnabled = if (tracking) "Ein" else "Aus"

    val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            latitude = location.latitude
            longitude = location.longitude
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { tracking = !tracking }) {
            Text(text = "GPS-Tracking: $btnTextTrackingEnabled")
        }
    }

    DisposableEffect(tracking) {
        if (tracking) {
            Log.d("Debug", "Tracking wird gestartet")
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 0f, locationListener)
            Log.d("Debug", "Longitiude: $longitude\nLatitude: $latitude")
        } else {
            Log.d("Debug", "Tracking wird gestoppt")
            locationManager.removeUpdates(locationListener)
        }

        onDispose {
            // Cleanup, if necessary
        }
    }
}
