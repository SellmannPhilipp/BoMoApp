package com.praktikum.bomoapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
<<<<<<< HEAD
=======
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
>>>>>>> GUI
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
<<<<<<< HEAD
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.praktikum.bomoapp.activities.MapActivity
import com.praktikum.bomoapp.activities.SettingsActivity
=======
import androidx.compose.runtime.Composable
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue



import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.remember

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Switch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider

>>>>>>> GUI

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
<<<<<<< HEAD
=======
            /*
>>>>>>> GUI
            val intentSettings = Intent(this, SettingsActivity::class.java)
            val intentMap = Intent(this, MapActivity::class.java)

            Column {
                Button(onClick = { startActivity(intentMap) }) {
                    Text(text = "Zeige Map")
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(onClick = { startActivity(intentSettings) }) {
                    Text(text = "Zeige Settings")
<<<<<<< HEAD
=======

                }
            }
            */
            AppContent()
        }
    }


    @Composable
    fun AppContent() {
        val tabOptions = listOf("Karte", "Einstellungen") // Füge hier die gewünschten Tab-Optionen hinzu
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
    fun GridContent() {
        var gridTexts = List(6) { MutableList(4) { "" } }

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
                        ) {
                            Text(
                                text = gridTexts[i][j],
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun SpinnerComponent() {
        val options = listOf("Fastes", "Game", "UI", "Normal")
        var expanded by remember { mutableStateOf(false) }
        var selectedIndex by remember { mutableStateOf(0) }

        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .border(1.dp, Color.Gray)
                .clickable { expanded = true }
        ) {
            Text(
                text = options[selectedIndex],
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp).align(Alignment.Center)
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
    fun FirstTabContent() {
        var switchState by remember { mutableStateOf(false) }

        val scrollState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 50.dp),
            state = scrollState
        ) {
            item {
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
                        checked = switchState,
                        onCheckedChange = { switchState = it },
                        modifier = Modifier.padding(horizontal = 200.dp)

                    )
                }

                Row {
                    Text(text = "Abtastrate",
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
                        onClick = { /* Hier kommt die entsprechende Aktion beim Klicken auf den Button */ },
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                    ) {
                        Text(text = "Speichern"
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
>>>>>>> GUI
                }
            }
        }
    }
}


