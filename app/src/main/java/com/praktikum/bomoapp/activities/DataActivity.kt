package com.praktikum.bomoapp.activities

import AccelerometerListenerSingleton
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.praktikum.bomoapp.ForegroundService
import com.praktikum.bomoapp.Singletons.GpsLocationListenerSingleton
import com.praktikum.bomoapp.Singletons.GyroscopeSensorEventListenerSingleton

val context = LocalContext
var myForegroundService: ForegroundService? = null
private var isBound = false
var serviceIntent: Intent? = null
private lateinit var sharedPreferences: SharedPreferences
var gridTexts = List(6) { MutableList(4) { "" } }

var gyroscopeX by mutableStateOf(0f)
var gyroscopeY by mutableStateOf(0f)
var gyroscopeZ by mutableStateOf(0f)

var accelerometerX by mutableStateOf(0f)
var accelerometerY by mutableStateOf(0f)
var accelerometerZ by mutableStateOf(0f)

var latitude by mutableStateOf(0.0)
var longitude by mutableStateOf(0.0)


@Composable
fun Data() {
    // Initialisieren Sie die SharedPreferences
    sharedPreferences = com.praktikum.bomoapp.activities.context.current.getSharedPreferences("my_preferences", ComponentActivity.MODE_PRIVATE)
    serviceIntent = Intent(LocalContext.current, ForegroundService::class.java)

    //UI
    val scrollState = rememberLazyListState()

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 50.dp),
        state = scrollState){
        item{
            //SpinnerComponent()
        }
        item {
            GridContent()
        }
        item {
            GraphContetn()
        }
    }
}

@Composable
fun SpinnerComponent() {
    val options = listOf("Fastes", "Game", "UI", "Normal")
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(sharedPreferences.getInt("selectedIndex", 0)) }

    Row {
        Text(
            text = "Abtastrate",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .border(1.dp, Color.Gray)
                .clickable { expanded = true }
        ) {
            Text(
                text = options[selectedIndex],
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center)
            )
            if (expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    options.forEachIndexed { index, option ->
                        Text(
                            text = option,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedIndex = index
                                    expanded = false
                                    writeInsharedPreferences("selectedIndex", selectedIndex)
                                }
                                .padding(16.dp)
                        )
                        if (index < options.size - 1) {
                            Divider(color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun GridContent() {
    /*
    val sensorManager: SensorManager = LocalContext.current.getSystemService(SENSOR_SERVICE) as SensorManager
    var locationManager: LocationManager = LocalContext.current.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    val gyroscopeSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    val accelerometerSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)


    if (ActivityCompat.checkSelfPermission(
            LocalContext.current,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            LocalContext.current,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, object :
            LocationListener {
            override fun onLocationChanged(location: Location) {
                latitude = location.latitude
                longitude = location.longitude
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        })


    if (gyroscopeSensor != null) {
        sensorManager.registerListener(
            object : SensorEventListener {
                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

                override fun onSensorChanged(event: SensorEvent?) {
                    if (event != null) {
                        gyroscopeX = event.values[0]
                        gyroscopeY = event.values[1]
                        gyroscopeZ = event.values[2]
                        //Log.d("Gyroscope", "$gyroscopeX\n$gyroscopeY\n$gyroscopeZ")
                    }
                }
            },
            gyroscopeSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    if (accelerometerSensor != null) {
        sensorManager.registerListener(
            object : SensorEventListener {
                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

                override fun onSensorChanged(event: SensorEvent?) {
                    if (event != null) {
                        accX = event.values[0]
                        accY = event.values[1]
                        accZ = event.values[2]
                        //Log.d("Gyroscope", "$gyroscopeX\n$gyroscopeY\n$gyroscopeZ")
                    }
                }
            },
            accelerometerSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }
     */

    accelerometerX = AccelerometerListenerSingleton.accelerometerX
    accelerometerY = AccelerometerListenerSingleton.accelerometerY
    accelerometerZ = AccelerometerListenerSingleton.accelerometerZ

    gyroscopeX = GyroscopeSensorEventListenerSingleton.gyroscopeX
    gyroscopeY = GyroscopeSensorEventListenerSingleton.gyroscopeY
    gyroscopeZ = GyroscopeSensorEventListenerSingleton.gyroscopeZ

    latitude = GpsLocationListenerSingleton.latitude
    longitude = GpsLocationListenerSingleton.longitude

    gridTexts = gridTexts.toMutableList().also { it[0][0] = "Gyroscope" }
    gridTexts = gridTexts.toMutableList().also { it[0][2] = "Acc" }
    gridTexts = gridTexts.toMutableList().also { it[1][0] = "X:" }
    gridTexts = gridTexts.toMutableList().also { it[1][2] = "X:" }
    gridTexts = gridTexts.toMutableList().also { it[2][0] = "Y:" }
    gridTexts = gridTexts.toMutableList().also { it[2][2] = "Y:" }
    gridTexts = gridTexts.toMutableList().also { it[3][0] = "Z:" }
    gridTexts = gridTexts.toMutableList().also { it[3][2] = "Z:" }
    gridTexts = gridTexts.toMutableList().also { it[4][0] = "latitude:" }
    gridTexts = gridTexts.toMutableList().also { it[4][2] = "longitude:" }
    Row {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .border(1.dp, Color.Black)
                        .padding(4.dp)
                        .height(40.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = gridTexts[0][0],
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Box(
                    modifier = Modifier
                        .border(1.dp, Color.Black)
                        .padding(4.dp)
                        .height(40.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = gridTexts[0][2],
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            for (i in 1 until 5) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (j in 0 until 4) {
                        Box(
                            modifier = Modifier
                                .border(1.dp, Color.Black)
                                .padding(4.dp)
                                .height(40.dp)
                                .weight(1f)
                        ) {
                            if (j % 2 == 0) {
                                Text(
                                    text = gridTexts[i][j],
                                    //text = textTest,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            } else {
                                when (i) {
                                    1 -> {
                                        if (j == 1)
                                            Text(
                                                text = gyroscopeX.toString(),
                                                //text = textTest,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.align(Alignment.Center)
                                            )
                                        if (j == 3)
                                            Text(
                                                text = accelerometerX.toString(),
                                                //text = textTest,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.align(Alignment.Center)
                                            )
                                    }

                                    2 -> {
                                        if (j == 1)
                                            Text(
                                                text = gyroscopeY.toString(),
                                                //text = textTest,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.align(Alignment.Center)
                                            )
                                        if (j == 3)
                                            Text(
                                                text = accelerometerY.toString(),
                                                //text = textTest,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.align(Alignment.Center)
                                            )
                                    }

                                    3 -> {
                                        if (j == 1)
                                            Text(
                                                text = gyroscopeZ.toString(),
                                                //text = textTest,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.align(Alignment.Center)
                                            )
                                        if (j == 3)
                                            Text(
                                                text = accelerometerZ.toString(),
                                                //text = textTest,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.align(Alignment.Center)
                                            )
                                    }

                                    4 -> {
                                        if (j == 1)
                                            Text(
                                                text = latitude.toString(),
                                                //text = textTest,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.align(Alignment.Center)
                                            )
                                        if (j == 3)
                                            Text(
                                                text = longitude.toString(),
                                                //text = textTest,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.align(Alignment.Center)
                                            )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GraphContetn() {
    TODO("Not yet implemented")
}

/*
@Composable
fun FirstTabContent() {
    sharedPreferences = context.current.getSharedPreferences("my_preferences", ComponentActivity.MODE_PRIVATE)

    // val sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(ContextAmbient.current)

    var switchStateSensor by remember { mutableStateOf(sharedPreferences.getBoolean("switchStateSensor", false)) }
    var switchStateType__ by remember { mutableStateOf(sharedPreferences.getBoolean("switchStateType__", false)) }

    val scrollState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 50.dp),
        state = scrollState
    ) {
        item {
            //Sensor Einschalten
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                Text(
                    text = "Senor einschalten",
                    modifier = Modifier,
                    textAlign = TextAlign.Center
                )
                Switch(
                    checked = switchStateSensor,
                    onCheckedChange = {switchStateSensor = it},
                    modifier = Modifier.padding(horizontal = 200.dp)
                )

                if (switchStateSensor) {
                    sharedPreferences = context.current.getSharedPreferences("my_preferences", ComponentActivity.MODE_PRIVATE)
                    //Start Service
                    serviceIntent?.let {
                        ContextCompat.startForegroundService(LocalContext.current,
                            it
                        )
                    }
                    with(sharedPreferences.edit()) {
                        putBoolean("switchStateSensor", switchStateSensor)
                        apply()
                    }
                } else {
                    //stopService(context.current, serviceIntent)
                    with(sharedPreferences.edit()) {
                        putBoolean("switchStateSensor", switchStateSensor)
                        apply()
                    }
                }
            }
            // Type__
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                Text(
                    text = "Type__",
                    modifier = Modifier,
                    textAlign = TextAlign.Center
                )
                Switch(
                    checked = switchStateType__,
                    onCheckedChange = {switchStateType__ = it},
                    modifier = Modifier.padding(horizontal = 200.dp)
                )


                if (switchStateType__) {
                    sharedPreferences = context.current.getSharedPreferences("my_preferences", ComponentActivity.MODE_PRIVATE)
                    //Start Service
                    serviceIntent?.let {
                        ContextCompat.startForegroundService(LocalContext.current,
                            it
                        )
                    }
                    with(sharedPreferences.edit()) {
                        putBoolean("switchStateType__", switchStateType__)
                        apply()
                    }
                } else {
                    //stopService(serviceIntent)
                    with(sharedPreferences.edit()) {
                        putBoolean("switchStateType__", switchStateSensor)
                        apply()
                    }
                }
            }
            //Abtastrate
            Row {
                Text(
                    text = "Abtastrate",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                SpinnerComponent()
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Daten Speichern",
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                )
                Button(
                    onClick = {

                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                ) {
                    Text(
                        text = "Speichern"
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)

            )
            {
                GridContent()
            }
        }
    }
}
*/

private val connection: ServiceConnection = object : ServiceConnection {
    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        val binder = service as ForegroundService.LocalBinder
        myForegroundService = binder.getService()
        isBound = true

        // Sie können nun auf Methoden und Attribute des Dienstes zugreifen
        //Log.d("Debug", textTest)
        //textTest = myForegroundService?.getTextService().toString()
        //Log.d("Debug", textTest)
    }

    override fun onServiceDisconnected(name: ComponentName) {
        myForegroundService = null
    }
}
private fun writeInsharedPreferences(Key: String, value: Int){
    with(sharedPreferences.edit()) {
        putInt(Key, value)
        apply()
    }
}
/*
override fun onStart() {
    super.onStart()
    val serviceIntent = Intent(this, ForegroundService::class.java)
    bindService(serviceIntent, connection, ComponentActivity.BIND_AUTO_CREATE)
}

override fun onStop() {
    super.onStop()
    unbindService(connection)
}

 */