package com.praktikum.bomoapp.activities

import AccelerometerListenerSingleton
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.axis.DataCategoryOptions
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarChartType
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import co.yml.charts.ui.barchart.models.SelectionHighlightData
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.praktikum.bomoapp.ForegroundService
import com.praktikum.bomoapp.R
import com.praktikum.bomoapp.Singletons.GpsLocationListenerSingleton
import com.praktikum.bomoapp.Singletons.GyroscopeSensorEventListenerSingleton
import com.praktikum.bomoapp.Singletons.MagnetoSensorListenerSingleton

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

var orientation by mutableStateOf(0f)

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
            GraphContent()
        }

        item {
            CompassGridContent()
        }
        item {
            CompassContent()
        }
        item {
            ChartContent()
        }




    }
}

@Composable
fun ChartContent() {
    val maxRange = 10
    val barData1 = BarData(Point(AccelerometerListenerSingleton.accelerometerX,0f), Color.Red,"accY",)
    val barData2 = BarData(Point(AccelerometerListenerSingleton.accelerometerY,1f), Color.Blue,"accX",)
    val barData3 = BarData(Point(AccelerometerListenerSingleton.accelerometerZ,2f), Color.Green,"accZ",)

    var barData = mutableListOf<BarData>()
    barData.add(barData1)
    barData.add(barData2)
    barData.add(barData3)

    val xStepSize = 5

    val xAxisData = AxisData.Builder()
        .steps(xStepSize)
        .bottomPadding(12.dp)
        .endPadding(40.dp)
        .labelData { index -> (index * (maxRange / xStepSize)).toString() }
        .build()
    val yAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .bottomPadding(12.dp)
        .steps(barData.size - 1)
        .labelAndAxisLinePadding(20.dp)
        .axisOffset(20.dp)
        .setDataCategoryOptions(
            DataCategoryOptions(
                isDataCategoryInYAxis = true,
                isDataCategoryStartFromBottom = false
            )
        )
        .startDrawPadding(18.dp)
        .labelData { index -> barData[index].label }
        .build()
    val barChartData = BarChartData(
        chartData = barData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        barStyle = BarStyle(
            isGradientEnabled = false,
            paddingBetweenBars = 20.dp,
            barWidth = 35.dp,
            selectionHighlightData = SelectionHighlightData(
                highlightBarColor = Color.Red,
                highlightTextBackgroundColor = Color.Green,
                popUpLabel = { x, _ -> " Value : $x " },
                barChartType = BarChartType.HORIZONTAL
            ),
        ),
        showYAxis = true,
        showXAxis = true,
        horizontalExtraSpace = 20.dp,
        barChartType = BarChartType.HORIZONTAL
    )
    BarChart(
        modifier = Modifier.height(220.dp),
        barChartData = barChartData
    )
}

@Composable
fun CompassContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box() {
            Image(
                painter = painterResource(id = R.drawable.compass_frame),
                contentDescription = stringResource(id = R.string.compass_content_description),
                //modifier = Modifier.rotate(-avgOrientation)
            )
            Image(
                painter = painterResource(id = R.drawable.compass_inner),
                contentDescription = stringResource(id = R.string.compass_content_description),
                modifier = Modifier.rotate(-orientation)
            )
        }
    }
}

@Composable
fun CompassGridContent() {
    // Compass Row UI
    Row (modifier = Modifier.fillMaxWidth()){
        Box(
            modifier = Modifier
                .border(1.dp, Color.Black)
                .padding(4.dp)
                .height(40.dp)
                .weight(1f)
        ) {
            Text(
                text = gridTexts[5][0],
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Box(
            modifier = Modifier
                .border(1.dp, Color.Black)
                .padding(4.dp)
                .height(40.dp)
                .weight(3f)

        ) {
            Text(
                text = orientation.toString(),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
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

    orientation = MagnetoSensorListenerSingleton.orientation

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
    gridTexts = gridTexts.toMutableList().also { it[5][0] = "Compass:" }

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
                                                text =  accelerometerX.toString(),
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
fun GraphContent() {
    accelerometerX = AccelerometerListenerSingleton.accelerometerX

    val pointsData: List<Point> =
        listOf(Point(0f, AccelerometerListenerSingleton.test[0]),
            Point(1f, AccelerometerListenerSingleton.test[1]),
            Point(2f, AccelerometerListenerSingleton.test[2]),
            Point(3f, AccelerometerListenerSingleton.test[3]),
            Point(4f, AccelerometerListenerSingleton.test[4]),
            Point(5f, AccelerometerListenerSingleton.test[5]),
            Point(6f, AccelerometerListenerSingleton.test[6]),
            Point(7f, AccelerometerListenerSingleton.test[7]),
            Point(8f, AccelerometerListenerSingleton.test[8]),
            Point(9f, AccelerometerListenerSingleton.test[9]))
    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .backgroundColor(Color.Blue)
        .steps(pointsData.size - 1)
        .labelData { i -> i.toString() }
        .labelAndAxisLinePadding(15.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(10)
        .backgroundColor(Color.Red)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            val yMin = -10
            val yMax = 10
            val yScale = (yMax - yMin)/10
            (i * yScale + yMin).toString()
        }.build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    LineStyle(),
                    IntersectionPoint(),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp()
                )
            ),
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(),
        backgroundColor = Color.White
    )
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        lineChartData = lineChartData
    )

    Row(modifier = Modifier.fillMaxWidth()) {
    Text(
        text =  AccelerometerListenerSingleton.test[0].toString() + ", " +
                AccelerometerListenerSingleton.test[1].toString() + ", " +
                AccelerometerListenerSingleton.test[2].toString() + ", " +
                AccelerometerListenerSingleton.test[3].toString() + ", " +
                AccelerometerListenerSingleton.test[4].toString() + ", " +
                AccelerometerListenerSingleton.test[5].toString() + ", " +
                AccelerometerListenerSingleton.test[6].toString() + ", " +
                AccelerometerListenerSingleton.test[7].toString() + ", " +
                AccelerometerListenerSingleton.test[8].toString() + ", " , //accelerometerX.toString(),//accelerometerX.toString(),
        //text = textTest,
        textAlign = TextAlign.Center,
        )
    }
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