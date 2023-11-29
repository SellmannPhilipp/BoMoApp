package com.praktikum.bomoapp.activities

import AccelerometerViewModel
import GpsTrackingViewModel
import GyroscopeViewModel
import MagnetometerViewModel
import android.content.Context
import android.hardware.SensorManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.praktikum.bomoapp.viewmodels.MeasurementViewModel
import com.praktikum.bomoapp.viewmodels.RouteViewModel
import com.praktikum.bomoapp.viewmodels.SamplingRateViewModel
import org.osmdroid.util.GeoPoint

@Composable
fun Settings() {
    val tabOptions = listOf("Position", "Sensorik", "Speichern") // Füge hier die gewünschten Tab-Optionen hinzu
    var selectedTabIndex by remember { mutableStateOf(0) }

    //val networkViewModel = NetworkTrackingViewModel(LocalContext.current)
    val gpsViewModel = GpsTrackingViewModel(LocalContext.current)
    val accViewModel = AccelerometerViewModel(LocalContext.current)
    val gyrViewModel = GyroscopeViewModel(LocalContext.current)
    val mgnViewModel = MagnetometerViewModel(LocalContext.current)
    val measurementViewModel = MeasurementViewModel(LocalContext.current)

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
                    //NetworkTracking(networkViewModel)
                    //Spacer(modifier = Modifier.height(20.dp))
                    MenuPath()
                    Spacer(modifier = Modifier.height(20.dp))
                    MenuPosition()
                    Spacer(modifier = Modifier.height(20.dp))
                    PositionTracking(gpsViewModel)
                    //Spacer(modifier = Modifier.height(20.dp))
                    //Marker()
                    Spacer(modifier = Modifier.height(20.dp))
                    Measurement(measurementViewModel)
                    Spacer(modifier = Modifier.height(20.dp))
                    AddMeasuringPoint(measurementViewModel)
                    Spacer(modifier = Modifier.height(20.dp))
                    ShowTrackedMeasurementPoints(measurementViewModel)
                    Spacer(modifier = Modifier.height(20.dp))
                    InterpolatedPoints()
                }
            }
        }
        1 -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    MenuSamplingrate()
                    Spacer(modifier = Modifier.height(20.dp))
                    Accelerometer(accViewModel, mgnViewModel)
                    Spacer(modifier = Modifier.height(20.dp))
                    Gyroscope(gyrViewModel)
                    Spacer(modifier = Modifier.height(20.dp))
                    Compass(mgnViewModel, accViewModel)
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
    val context = LocalContext.current
    Button(onClick = { DataSaver.saveAllData(context)}) {
        Text(text = "Daten auf Server Speichern")
    }
}


/*
//Function to track location by network provider
@SuppressLint("MissingPermission")
@Composable
fun NetworkTracking(viewModel: NetworkTrackingViewModel) {
    var btnTextEnabled = if (viewModel.tracking) "Ein" else "Aus"


    Button(onClick = { viewModel.toggleTracking() }) {
        Text(text = "Network-Tracking: $btnTextEnabled")
    }

} */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuPosition() {
    var isExpanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Pos Variante") }
    var sharedPreferences = LocalContext.current.getSharedPreferences("GPS-Tracking",Context.MODE_PRIVATE)

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = {isExpanded = it}
    ) {
        TextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = "ACCURACY_HIGH")},
                onClick = {
                    selectedText = "ACCURACY_HIGH"
                    with(sharedPreferences.edit()) {
                        putInt("priority",1)
                        apply()
                    }
                    Log.d("Priority setting", sharedPreferences.getInt("priority",5).toString())
                    isExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(text = "BALANCED_POWER_ACCURACY")},
                onClick = {
                    selectedText = "BALANCED_POWER_ACCURACY"
                    with(sharedPreferences.edit()) {
                        putInt("priority",2)
                        apply()
                    }
                    Log.d("Priority setting", sharedPreferences.getInt("priority",5).toString())
                    isExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(text = "GPS_PROVIDER")},
                onClick = {
                    selectedText = "GPS_PROVIDER"
                    with(sharedPreferences.edit()) {
                        putInt("priority",3)
                        apply()
                    }
                    Log.d("Priority setting", sharedPreferences.getInt("priority",5).toString())
                    isExpanded = false
                }
            )
        }
    }
}

@Composable
fun PositionTracking(viewModel: GpsTrackingViewModel) {
    var btnTextEnabled = if (viewModel.tracking) "Ein" else "Aus"


    Button(onClick = { viewModel.toggleTracking() }) {
        Text(text = "Position-Tracking: $btnTextEnabled")
    }
}

@Composable
fun Accelerometer(viewModel: AccelerometerViewModel, magnetometer: MagnetometerViewModel) {
    var context: Context = LocalContext.current
    var btnTextEnabled = if (viewModel.tracking) "Ein" else "Aus"

    Button(
        onClick = {
            if(viewModel.tracking) {
                if(magnetometer.tracking) {
                    Toast.makeText(context, "Kompass muss zuerst deaktiviert werden", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.toggleAccelerometer(SamplingRateViewModel.samplingRate)
                }
            } else {
                viewModel.toggleAccelerometer(SamplingRateViewModel.samplingRate)
            }
        }
    ) {
        Text(text = "Beschleunigungssensor: $btnTextEnabled")
    }
}

@Composable
fun Gyroscope(viewModel: GyroscopeViewModel) {
    var btnTextEnabled = if (viewModel.tracking) "Ein" else "Aus"

    Button(onClick = { viewModel.toggleGyroscope(SamplingRateViewModel.samplingRate) }) {
        Text(text = "Gyroskop: $btnTextEnabled")
    }
}

@Composable
fun Compass(viewModel: MagnetometerViewModel, acceleromter: AccelerometerViewModel) {
    var context: Context = LocalContext.current
    var btnTextEnabled = if (viewModel.tracking) "Ein" else "Aus"
    Button(
        onClick = {
            if(!viewModel.tracking) {
                if(acceleromter.tracking) {
                    viewModel.toggleMagnetometer((SamplingRateViewModel.samplingRate))
                } else {
                    Toast.makeText(context, "Beschleunigungssensor muss aktiviert werden", Toast.LENGTH_SHORT).show()
                }
            } else {
                viewModel.toggleMagnetometer((SamplingRateViewModel.samplingRate))
            }
        }
    ) {
        Text(text = "Kompass: $btnTextEnabled")
    }
}

/*
@Composable
fun Marker() {
    Button(
        onClick = {
            if(LastLocationViewModel.locationList.size > 1){
                LastLocationViewModel.getLastLocation()
            }
         }
    ) {
        Text(text = "Markiere letzte bekannte Position")
    }
} */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuSamplingrate() {
    var isExpanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Samplingrate") }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = {isExpanded = it}
    ) {
        TextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )
        
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = "Normal")},
                onClick = {
                    selectedText = "Normal"
                    SamplingRateViewModel.samplingRate = SensorManager.SENSOR_DELAY_NORMAL
                    isExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(text = "Fastest")},
                onClick = {
                    selectedText = "Fastest"
                    SamplingRateViewModel.samplingRate = SensorManager.SENSOR_DELAY_FASTEST
                    isExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(text = "Game")},
                onClick = {
                    selectedText = "Game"
                    SamplingRateViewModel.samplingRate = SensorManager.SENSOR_DELAY_GAME
                    isExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(text = "UI")},
                onClick = {
                    selectedText = "UI"
                    SamplingRateViewModel.samplingRate = SensorManager.SENSOR_DELAY_UI
                    isExpanded = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuPath() {
    var isExpanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Route") }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = {isExpanded = it}
    ) {
        TextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = "Ausblenden")},
                onClick = {
                    selectedText = "Ausblenden"
                    RouteViewModel.setSelectedRoute(0)
                    isExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(text = "Route 1")},
                onClick = {
                    selectedText = "Route 1"
                    RouteViewModel.setSelectedRoute(1)
                    isExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(text = "Route 2")},
                onClick = {
                    selectedText = "Route 2"
                    RouteViewModel.setSelectedRoute(2)
                    isExpanded = false
                }
            )
        }
    }
}

@Composable
fun Measurement(viewModel: MeasurementViewModel) {
    var context: Context = LocalContext.current
    var btnTextEnabled = if (viewModel.measurement) "Ein" else "Aus"
    Button(
        onClick = {
            viewModel.toggleMeasurement()
        }
    ) {
        Text(text = "Messung: $btnTextEnabled")
    }
}

@Composable
fun AddMeasuringPoint(viewModel: MeasurementViewModel) {
    var context: Context = LocalContext.current
    Button(
        onClick = {
            if(viewModel.measurement) {
                var lastLocation = DataSaver.gpsList.get(DataSaver.gpsList.size - 1)
                var fragments = lastLocation.split(",")
                MeasurementViewModel.addUserMeasuringPoint(GeoPoint(fragments[1].toDouble(), fragments[2].toDouble()), fragments[0].toLong())
                Toast.makeText(context, "Messpunkt aufgenommen", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Messung muss aktiv sein", Toast.LENGTH_SHORT).show()
            }
        }
    ) {
        Text(text = "Messupunkt aufnehmen")
    }
}

@Composable
fun ShowTrackedMeasurementPoints(viewModel: MeasurementViewModel) {
    var context: Context = LocalContext.current
    Button(
        onClick = {
            if(MeasurementViewModel.showTrackedMeasuringPoints) {
                MeasurementViewModel.showTrackedMeasuringPoints = false
            } else {
                MeasurementViewModel.showTrackedMeasuringPoints = true
            }
        }
    ) {
        Text(text = "Zeige Messupunkte")
    }
}

@Composable
fun InterpolatedPoints() {
    var context: Context = LocalContext.current
    Button(
        onClick = {
            //Toast.makeText(context, "Noch nicht implementiert", Toast.LENGTH_SHORT).show()
            RouteViewModel.showInterpolated = !RouteViewModel.showInterpolated
        }
    ) {
        Text(text = "Zeige interpolierte Punkte")
    }
}

