package com.praktikum.bomoapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val intentSettings = Intent(this, SettingsActivity::class.java)
            val intentMap = Intent(this, MapActivity::class.java)

            Column {
                Button(onClick = { startActivity(intentMap) }) {
                    Text(text = "Zeige Map")
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(onClick = { startActivity(intentSettings) }) {
                    Text(text = "Zeige Settings")
                }
            }
        }
    }
}


