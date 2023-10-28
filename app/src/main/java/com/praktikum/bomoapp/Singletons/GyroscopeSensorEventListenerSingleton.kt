package com.praktikum.bomoapp.Singletons

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.praktikum.bomoapp.DataSaver

object GyroscopeSensorEventListenerSingleton {
    private var instance: SensorEventListener? = null

    var gyroscopeX by mutableStateOf(0f)
    var gyroscopeY by mutableStateOf(0f)
    var gyroscopeZ by mutableStateOf(0f)

    fun getInstance(context: Context): SensorEventListener {
        if (instance == null) {
            instance = createSensorEventListener(context)
        }
        return instance!!
    }

    private fun createSensorEventListener(context: Context): SensorEventListener {
        return object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                // Hier kann ggf. die Genauigkeit des Sensors behandelt werden
            }

            override fun onSensorChanged(event: SensorEvent) {
                gyroscopeX = event.values[0]
                gyroscopeY = event.values[1]
                gyroscopeZ = event.values[2]
                Log.d("Gyroscope", "${event.values[0]}\n${event.values[1]}\n${event.values[2]}")
                DataSaver.gyroscopeList.add(System.currentTimeMillis().toString() + "," + event.values[0] + "," + event.values[1] + "," + event.values[2] + "\n")
            }
        }
    }
}