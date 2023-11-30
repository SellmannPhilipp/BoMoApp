package com.praktikum.bomoapp.viewmodels

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.praktikum.bomoapp.DataSaver
import com.praktikum.bomoapp.MeasuringPoint
import com.praktikum.bomoapp.activities.Data
import org.osmdroid.util.GeoPoint

class MeasurementViewModel(context: Context) : ViewModel() {

    companion object {
        var userTrackedMeasuringPoints = arrayListOf<MeasuringPoint>()
        var generalTrackedMeasuringPoints = arrayListOf<MeasuringPoint>()
        var showTrackedMeasuringPoints = false
        var interpolatedPoints = arrayListOf<MeasuringPoint>()
        var measurementActive = false

        fun addUserMeasuringPoint(location: GeoPoint, timestamp: Long) {
            userTrackedMeasuringPoints.add(MeasuringPoint(location, timestamp))
        }

        fun addGeneralMeasuringPoint(location: GeoPoint, timestamp: Long) {
            generalTrackedMeasuringPoints.add(MeasuringPoint(location, timestamp))
        }

        fun addUserMeasuringPoint() {
            var lastLocation = generalTrackedMeasuringPoints.last()
            addUserMeasuringPoint(lastLocation.getLocation(), lastLocation.getTimestamp())
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
        interpolatedPoints.clear()

        this.startTime = System.currentTimeMillis()
    }

    fun stop() {
        measurementActive = false
        this.endtime = System.currentTimeMillis()

        if(generalTrackedMeasuringPoints.isNotEmpty()) {
            addUserMeasuringPoint(generalTrackedMeasuringPoints.last().getLocation(), generalTrackedMeasuringPoints.last().getTimestamp())
            userTrackedMeasuringPoints.add(0, MeasuringPoint(generalTrackedMeasuringPoints.first().getLocation(), generalTrackedMeasuringPoints.first().getTimestamp()))
        }

        if(RouteViewModel.getSelectedRoute() == 1) {
            interpolatedPoints = RouteViewModel.interpolateRoute(RouteViewModel.polylinePointsOne) as ArrayList<MeasuringPoint>
        } else if(RouteViewModel.getSelectedRoute() == 2) {
            interpolatedPoints = RouteViewModel.interpolateRoute(RouteViewModel.polylinePointsTwo) as ArrayList<MeasuringPoint>
        }

        showInputDialog(ctx)
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
            DataSaver.writeMeasurement(enteredText, startTime, endtime, generalTrackedMeasuringPoints, userTrackedMeasuringPoints)
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
        }

        builder.show()
    }
}