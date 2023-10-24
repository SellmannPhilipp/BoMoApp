package com.praktikum.bomoapp.activities

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.praktikum.bomoapp.DataSaver
import com.praktikum.bomoapp.viewmodels.AccelerometerViewModel
import com.praktikum.bomoapp.viewmodels.GpsTrackingViewModel
import com.praktikum.bomoapp.viewmodels.GyroscopeViewModel
import com.praktikum.bomoapp.viewmodels.NetworkTrackingViewModel
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.io.File
import java.io.FileOutputStream
import kotlin.concurrent.thread


val gpsList = mutableListOf("")
val networkList = mutableListOf("")
val accelerometerList = mutableListOf("")
val gyroscopeList = mutableListOf("")
val lock = Any()

@Composable
fun Settings() {
    val networkViewModel = NetworkTrackingViewModel(LocalContext.current)
    val gpsViewModel = GpsTrackingViewModel(LocalContext.current)
    val accViewModel = AccelerometerViewModel(LocalContext.current)
    val gyrViewModel = GyroscopeViewModel(LocalContext.current)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            NetworkTracking(networkViewModel)
            Spacer(modifier = Modifier.height(20.dp))
            GpsTracking(gpsViewModel)
            Spacer(modifier = Modifier.height(20.dp))
            Accelerometer(accViewModel)
            Spacer(modifier = Modifier.height(20.dp))
            Gyroscope(gyrViewModel)
            Spacer(modifier = Modifier.height(20.dp))
            saveDataButton()
        }
    }
}

@Composable
fun saveDataButton() {
    Button(onClick = { DataSaver.saveAllDataServer()}) {
        Text(text = "Daten auf Server Speichern")
    }
}



//Function to track location by network provider
@SuppressLint("MissingPermission")
@Composable
fun NetworkTracking(viewModel: NetworkTrackingViewModel) {
    var btnTextEnabled = if (viewModel.tracking) "Ein" else "Aus"


    Button(onClick = { viewModel.toggleTracking() }) {
        Text(text = "Network-Tracking: $btnTextEnabled")
    }

}

@Composable
fun GpsTracking(viewModel: GpsTrackingViewModel) {
    var btnTextEnabled = if (viewModel.tracking) "Ein" else "Aus"


    Button(onClick = { viewModel.toggleTracking() }) {
        Text(text = "GPS-Tracking: $btnTextEnabled")
    }
}

@Composable
fun Accelerometer(viewModel: AccelerometerViewModel) {
    var btnTextEnabled = if (viewModel.accelerometerOn) "Ein" else "Aus"

    Button(onClick = { viewModel.toggleAccelerometer() }) {
        Text(text = "Beschleunigungssensor: $btnTextEnabled")
    }
}

@Composable
fun Gyroscope(viewModel: GyroscopeViewModel) {
    var btnTextEnabled = if (viewModel.gyroscopeOn) "Ein" else "Aus"

    Button(onClick = { viewModel.toggleGyroscope() }) {
        Text(text = "Gyroskop: $btnTextEnabled")
    }
}