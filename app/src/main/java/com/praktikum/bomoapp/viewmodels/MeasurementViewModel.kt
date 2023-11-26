package com.praktikum.bomoapp.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MeasurementViewModel(context: Context) : ViewModel() {
    private var startTime: Long = 0
    private var endtime: Long = 0
    private var ctx = context
    var measurement by mutableStateOf(false)

    fun start() {
        this.startTime = System.currentTimeMillis()
    }

    fun stop() {
        this.endtime = System.currentTimeMillis()
    }

    fun toggleMeasurement() {
        measurement = !measurement

        // SharedPreferences-Instanz abrufen
        val sharedPreferences = ctx.getSharedPreferences("Measurement", Context.MODE_PRIVATE)

        // Editor zum Bearbeiten der SharedPreferences
        val editor = sharedPreferences.edit()

        // Den Wert von 'measurement' speichern
        editor.putBoolean("measurement", measurement)

        // Änderungen speichern
        editor.apply()

        if(measurement) {
            start()
        } else {
            stop()
        }
    }
}