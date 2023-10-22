package com.praktikum.bomoapp.viewmodels

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class GyroscopeViewModel(context: Context) : ViewModel() {
    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var sensorEventListener: SensorEventListener? = null

    var gyroscopeOn by mutableStateOf(false)

    var gyrX by mutableStateOf(0f)
        private set

    var gyrY by mutableStateOf(0f)
        private set

    var gyrZ by mutableStateOf(0f)
        private set

    init {
        sensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                // Hier kann ggf. die Genauigkeit des Sensors behandelt werden
            }

            override fun onSensorChanged(event: SensorEvent) {
                gyrX = event.values[0]
                gyrY = event.values[1]
                gyrZ = event.values[2]
                Log.d("Debug", "$gyrX\n$gyrY\n$gyrZ")
            }
        }
    }

    fun toggleGyroscope() {
        gyroscopeOn = !gyroscopeOn

        if (gyroscopeOn) {
            viewModelScope.launch {
                sensorManager.registerListener(
                    sensorEventListener,
                    sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                    SensorManager.SENSOR_DELAY_NORMAL
                )
            }
        } else {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    override fun onCleared() {
        super.onCleared()
        gyroscopeOn = false
        sensorManager.unregisterListener(sensorEventListener)
    }
}