package com.praktikum.bomoapp.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MeasurementViewModel(context: Context) : ViewModel() {
    private var ctx = context
    var measurement by mutableStateOf(false)

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
    }
}