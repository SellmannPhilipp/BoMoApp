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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if(ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                settings()
            }
            else {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),0)
            }
        }
    }
}

@Composable
fun settings() {
    Column {
        NetworkTracking()
        Spacer(modifier = Modifier.height(20.dp))
        GpsTracking()
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
            Log.d("Network-Tracking", "Tracking wird gestartet")
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 0f, locationListener)
            Log.d("Network-Tracking", "Longitiude: $longitude\nLatitude: $latitude")
        } else {
            Log.d("Network-Tracking", "Tracking wird gestoppt")
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
            Log.d("GPS-Tracking", "Tracking wird gestartet")
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 0f, locationListener)
            Log.d("GPS-Tracking", "Longitiude: $longitude\nLatitude: $latitude")
        } else {
            Log.d("GPS-Tracking", "Tracking wird gestoppt")
            locationManager.removeUpdates(locationListener)
        }

        onDispose {
            // Cleanup, if necessary
        }
    }
}
