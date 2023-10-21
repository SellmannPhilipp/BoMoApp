package com.praktikum.bomoapp.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.praktikum.bomoapp.ForegroundService

class MainActivity : ComponentActivity() {

    private var myForegroundService: ForegroundService? = null
    private var isBound = false
    var serviceIntent: Intent? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var textTest by mutableStateOf("Das")
    var gridTexts = List(6) { MutableList(4) { "" } }
    var gridGX: String  by mutableStateOf("0")
    var gridGY: String by mutableStateOf("0")
    var gridGZ: String by mutableStateOf("0")
    var gridAX: String  by mutableStateOf("0")
    var gridAY: String by mutableStateOf("0")
    var gridAZ: String by mutableStateOf("0")
    var gridLat: String by mutableStateOf("0")
    var gridLong: String by mutableStateOf("0")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Initialisieren Sie die SharedPreferences
            sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)

            serviceIntent = Intent(LocalContext.current, ForegroundService::class.java)

            AppContent()
        }
    }


    @Composable
    fun AppContent() {
        val tabOptions =
            listOf("Karte", "Einstellungen") // Füge hier die gewünschten Tab-Optionen hinzu
        var selectedTabIndex by remember { mutableStateOf(0) }

        TabRow(selectedTabIndex) {
            tabOptions.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = title) }
                )
            }
        }
        //Hier sind die einzelnen Tabs
        when (selectedTabIndex) {
            0 -> {
                Text(text = "Karte")
            }
            1 -> {
                FirstTabContent()
            }
        }
    }

    @Composable
    fun SpinnerComponent() {
        val options = listOf("Fastes", "Game", "UI", "Normal")
        var expanded by remember { mutableStateOf(false) }
        var selectedIndex by remember { mutableStateOf(sharedPreferences.getInt("selectedIndex", 0)) }

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
                                    /*
                                    when (selectedIndex) {
                                        3 -> {
                                            writeInsharedPreferences("selectedIndex", selectedIndex)
                                        }
                                        2 -> {
                                            writeInsharedPreferences("selectedIndex", selectedIndex)
                                        }
                                        1 -> {
                                            writeInsharedPreferences("selectedIndex", selectedIndex)
                                        }
                                        0 -> {
                                            writeInsharedPreferences("selectedIndex", selectedIndex)
                                        }
                                    }
                                    */
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


    @Composable
    fun GridContent() {
        gridTexts = gridTexts.toMutableList().also { it[0][0] = "Gyroscope" }
        gridTexts = gridTexts.toMutableList().also { it[0][2] = "Acc" }
        gridTexts = gridTexts.toMutableList().also { it[1][0] = "X:" }
        gridTexts = gridTexts.toMutableList().also { it[1][2] = "X:" }
        gridTexts = gridTexts.toMutableList().also { it[2][0] = "Y:" }
        gridTexts = gridTexts.toMutableList().also { it[2][2] = "Y:" }
        gridTexts = gridTexts.toMutableList().also { it[3][0] = "Z:" }
        gridTexts = gridTexts.toMutableList().also { it[3][2] = "Z:" }

        gridTexts = gridTexts.toMutableList().also { it[3][3] = remember {
            textTest
        }
        }

        gridTexts = gridTexts.toMutableList().also { it[4][0] = "latitude:" }
        gridTexts = gridTexts.toMutableList().also { it[4][2] = "longitude:" }

        Column (modifier = Modifier.fillMaxWidth()){
            Row (modifier = Modifier.fillMaxWidth()){
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
            for (i in 1 until 6) {
                Row (modifier = Modifier.fillMaxWidth()){

                    for (j in 0 until 4) {
                        Box(
                            modifier = Modifier
                                .border(1.dp, Color.Black)
                                .padding(4.dp)
                                .height(40.dp)
                                .weight(1f)
                        ){
                        if(j % 2 == 0){
                                Text(
                                    text = gridTexts[i][j],
                                    //text = textTest,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                        }else{
                            when (i) {
                                1  ->{
                                    if (j == 1)
                                    Text(
                                        text = gridGX,
                                        //text = textTest,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.align(Alignment.Center)
                                        )
                                    if (j == 3)
                                        Text(
                                            text = gridAX,
                                            //text = textTest,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.align(Alignment.Center)
                                        )
                                    }
                                2 ->{
                                    if (j == 1)
                                        Text(
                                            text = gridGY,
                                            //text = textTest,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.align(Alignment.Center)
                                        )
                                    if (j == 3)
                                        Text(
                                            text = gridAY,
                                            //text = textTest,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.align(Alignment.Center)
                                        )
                                }
                                3 ->{
                                    if (j == 1)
                                        Text(
                                            text = gridGZ,
                                            //text = textTest,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.align(Alignment.Center)
                                        )
                                    if (j == 3)
                                        Text(
                                            text = gridAZ,
                                            //text = textTest,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.align(Alignment.Center)
                                        )
                                }
                                3 ->{
                                    if (j == 1)
                                        Text(
                                            text = gridLat,
                                            //text = textTest,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.align(Alignment.Center)
                                        )
                                    if (j == 3)
                                        Text(
                                            text = gridLong,
                                            //text = textTest,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.align(Alignment.Center)
                                        )
                                }
                                4 ->{
                                    if (j == 1)
                                        Text(
                                            text = gridLat,
                                            //text = textTest,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.align(Alignment.Center)
                                        )
                                    if (j == 3)
                                        Text(
                                            text = gridLong,
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

    @Composable
    fun FirstTabContent() {
        sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)

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
                        sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
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
                        stopService(serviceIntent)
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
                        sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
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
                        stopService(serviceIntent)
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
                                  /* Hier kommt die entsprechende Aktion beim Klicken auf den Button */
                            //Test zum Gui Akktualisieren
                            /*
                            myForegroundService?.textChange()
                            Log.d("Debug", textTest)
                            textTest = myForegroundService?.getTextService().toString()
                            gridTexts[3][3] = myForegroundService?.getTextService().toString()
                            gridGX = myForegroundService?.getTextService().toString()
                            gridAX = myForegroundService?.getTextService().toString()
                            */
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

    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundService.LocalBinder
            myForegroundService = binder.getService()
            isBound = true

            // Sie können nun auf Methoden und Attribute des Dienstes zugreifen
            Log.d("Debug", textTest)
            textTest = myForegroundService?.getTextService().toString()
            Log.d("Debug", textTest)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            myForegroundService = null
        }
    }

    override fun onStart() {
        super.onStart()
        val serviceIntent = Intent(this, ForegroundService::class.java)
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }
    private fun writeInsharedPreferences(Key: String, value: Int){
        sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt(Key, value)
            apply()
        }
    }
}
