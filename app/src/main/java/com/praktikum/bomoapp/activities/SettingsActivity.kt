package com.praktikum.bomoapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
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

class SettingsActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if(ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),0)
            }
            settings()
        }
    }
}

@Composable
fun settings() {
    Column {
        NetworkTracking()
        Spacer(modifier = Modifier.height(20.dp))
        GpsTracking()
        Spacer(modifier = Modifier.height(20.dp))
        Accelerometer()
        Spacer(modifier = Modifier.height(20.dp))
        Gyroscope()
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

    var btnTextEnabled = if (tracking) "Ein" else "Aus"

    val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            latitude = location.latitude
            longitude = location.longitude
        }
    }

    Button(onClick = { tracking = !tracking }) {
        Text(text = "Network-Tracking: $btnTextEnabled")
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

    var btnTextEnabled = if (tracking) "Ein" else "Aus"

    val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            latitude = location.latitude
            longitude = location.longitude
        }
    }

    Button(onClick = { tracking = !tracking }) {
        Text(text = "GPS-Tracking: $btnTextEnabled")
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

@Composable
fun Accelerometer() {
    val ctx = LocalContext.current
    var accelerometerOn by remember { mutableStateOf(false) }
    var btnTextEnabled = if (accelerometerOn) "Ein" else "Aus"
    var accX by remember { mutableStateOf(0f) }
    var accY by remember { mutableStateOf(0f) }
    var accZ by remember { mutableStateOf(0f) }

    val sensorManager: SensorManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    val sensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        }
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                accX = event.values[0]
                accY = event.values[1]
                accZ = event.values[2]
            }
        }
    }

    Button(onClick = { accelerometerOn = !accelerometerOn }) {
        Text(text = "Beschleunigungssensor: $btnTextEnabled")
    }

    DisposableEffect(accelerometerOn) {
        if (accelerometerOn) {
            Log.d("Accelerometer", "Accelerometer gestartet")
            sensorManager.registerListener(sensorEventListener,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL)
            Log.d("Accelerometer", "X: $accX\nY: $accY\nZ: $accZ")
        } else {
            Log.d("Accelerometer", "Accelerometer gestoppt")
            sensorManager.unregisterListener(sensorEventListener)
        }

        onDispose {
            // Cleanup, if necessary
        }
    }
}

@Composable
fun Gyroscope() {
    val ctx = LocalContext.current
    var gyroscopeOn by remember { mutableStateOf(false) }
    var btnTextEnabled = if (gyroscopeOn) "Ein" else "Aus"
    var gyrX by remember { mutableStateOf(0f) }
    var gyrY by remember { mutableStateOf(0f) }
    var gyrZ by remember { mutableStateOf(0f) }

    val sensorManager: SensorManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    val sensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        }
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
                gyrX = event.values[0]
                gyrY = event.values[1]
                gyrZ = event.values[2]
            }
        }
    }

    Button(onClick = { gyroscopeOn = !gyroscopeOn }) {
        Text(text = "Gyroskop: $btnTextEnabled")
    }

    DisposableEffect(gyroscopeOn) {
        if (gyroscopeOn) {
            Log.d("Gyroscope", "Gyroscope gestartet")
            sensorManager.registerListener(sensorEventListener,sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),SensorManager.SENSOR_DELAY_NORMAL)
            Log.d("Gyroscope", "X: $gyrX\nY: $gyrY\nZ: $gyrZ")
        } else {
            Log.d("Gyroscope", "Gyroscope gestoppt")
            sensorManager.unregisterListener(sensorEventListener)
        }

        onDispose {
            // Cleanup, if necessary
        }
    }
}