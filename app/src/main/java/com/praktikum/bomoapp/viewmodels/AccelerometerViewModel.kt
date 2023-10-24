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
import com.praktikum.bomoapp.DataSaver
import kotlinx.coroutines.launch

class AccelerometerViewModel(context: Context) : ViewModel() {
    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var sensorEventListener: SensorEventListener? = null

    var accelerometerOn by mutableStateOf(false)

    var accX by mutableStateOf(0f)
        private set

    var accY by mutableStateOf(0f)
        private set

    var accZ by mutableStateOf(0f)
        private set

    init {
        sensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                // Hier kann ggf. die Genauigkeit des Sensors behandelt werden
            }

            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    accX = event.values[0]
                    accY = event.values[1]
                    accZ = event.values[2]
                    Log.d("Accelerometer", "$accX\n$accY\n$accZ")
                    DataSaver.accelerometerList.add(System.currentTimeMillis().toString()+","+accX+","+accY+","+accZ+"\n")
                }
            }
        }
    }

    fun toggleAccelerometer() {
        accelerometerOn = !accelerometerOn

        if (accelerometerOn) {
            viewModelScope.launch {
                sensorManager.registerListener(
                    sensorEventListener,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL
                )
            }
        } else {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    override fun onCleared() {
        super.onCleared()
        accelerometerOn = false
        sensorManager.unregisterListener(sensorEventListener)
    }
}