package com.praktikum.bomoapp.viewmodels

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.widget.EditText
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.praktikum.bomoapp.MeasuringPoint
import org.osmdroid.util.GeoPoint

class MeasurementViewModel(context: Context) : ViewModel() {

    companion object {
        var userTrackedMeasuringPoints = arrayListOf<MeasuringPoint>()
        var generalTrackedMeasuringPoints = arrayListOf<MeasuringPoint>()
        var showTrackedMeasuringPoints = false
        var measurementActive = false

        fun addUserMeasuringPoint(location: GeoPoint, timestamp: Long) {
            userTrackedMeasuringPoints.add(MeasuringPoint(location, timestamp))
        }

        fun addGeneralMeasuringPoint(location: GeoPoint, timestamp: Long) {
            generalTrackedMeasuringPoints.add(MeasuringPoint(location, timestamp))
        }
    }

    private var startTime: Long = 0
    private var endtime: Long = 0
    private var ctx = context
    var measurement by mutableStateOf(false)

    init {
        val sharedPreferences = context.getSharedPreferences("Measurement", Context.MODE_PRIVATE)

        // Wert von 'tracking' aus den SharedPreferences wiederherstellen (mit einem Standardwert von 'false')
        measurement = sharedPreferences.getBoolean("measurement", false)
    }

    fun start() {
        measurementActive = true
        userTrackedMeasuringPoints.clear()
        generalTrackedMeasuringPoints.clear()
        this.startTime = System.currentTimeMillis()
    }

    fun stop() {
        measurementActive = false
        this.endtime = System.currentTimeMillis()
        showInputDialog(ctx)
    }

    fun getMeasuringPoints(): ArrayList<MeasuringPoint> {
        return userTrackedMeasuringPoints
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

        if (measurement) {
            start()
        } else {
            stop()
        }
    }

    fun showInputDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Dateiname eingeben")
        val input = EditText(context)
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, which ->
            val enteredText = input.text.toString()
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
        }

        builder.show()
    }
}