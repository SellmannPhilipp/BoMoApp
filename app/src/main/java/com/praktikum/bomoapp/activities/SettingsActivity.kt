package com.praktikum.bomoapp.activities

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.praktikum.bomoapp.ChromeTab
import com.praktikum.bomoapp.DataSaver
import com.praktikum.bomoapp.viewmodels.AccelerometerViewModel
import com.praktikum.bomoapp.viewmodels.GpsTrackingViewModel
import com.praktikum.bomoapp.viewmodels.GyroscopeViewModel
import com.praktikum.bomoapp.viewmodels.NetworkTrackingViewModel

@Composable
fun Settings() {
    val tabOptions = listOf("Positionierung", "Sensorik", "Speichern") // Füge hier die gewünschten Tab-Optionen hinzu
    var selectedTabIndex by remember { mutableStateOf(0) }

    val networkViewModel = NetworkTrackingViewModel(LocalContext.current)
    val gpsViewModel = GpsTrackingViewModel(LocalContext.current)
    val accViewModel = AccelerometerViewModel(LocalContext.current)
    val gyrViewModel = GyroscopeViewModel(LocalContext.current)

    TabRow(selectedTabIndex) {
        tabOptions.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { selectedTabIndex = index },
                text = { Text(text = title) }
            )
        }
    }
    when (selectedTabIndex) {
        0 -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    NetworkTracking(networkViewModel)
                    Spacer(modifier = Modifier.height(20.dp))
                    GpsTracking(gpsViewModel)
                }
            }
        }
        1 -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Accelerometer(accViewModel)
                    Spacer(modifier = Modifier.height(20.dp))
                    Gyroscope(gyrViewModel)
                    Spacer(modifier = Modifier.height(20.dp))
                    Compass()
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
        2 -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    SaveDataButton()
                    Spacer(modifier = Modifier.height(20.dp))
                    OpenBrowser()
                }
            }
        }
    }
}

@Composable
fun OpenBrowser() {
    val context = LocalContext.current
    Button(onClick = { ChromeTab.ShowFilesInBrowser(context)}) {
        Text(text = "Daten auf dem Server zeigen")
    }
}


@Composable
fun SaveDataButton() {
    Button(onClick = { DataSaver.saveAllData()}) {
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

@Composable
fun Compass() {
    Button(onClick = {  }) {
        Text(text = "Kompass")
    }
}