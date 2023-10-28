package com.praktikum.bomoapp.Singletons

import AccelerometerListenerSingleton
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.praktikum.bomoapp.DataSaver

var avgSmoothing = FloatArray(5)
fun movingAverage(newValue: Float): Float {
    var orientation = newValue
    if (orientation < 0) {
        orientation += 360f
    }

    for (i in 0 until (avgSmoothing.size - 1)) {
        avgSmoothing[i] = avgSmoothing[i + 1]
    }
    avgSmoothing[4] = orientation

    var sum = 0f
    avgSmoothing.forEach { sum += it }
    var avg = sum / (avgSmoothing.size)

    return if ((orientation - avg) > 180)
        orientation
    else
        avg
}


object MagnetoSensorListenerSingleton {
    private var instance: SensorEventListener? = null

    var magnetometerReading = FloatArray(3)
    var accelerometerReading = FloatArray(3)
    var orientation by mutableStateOf(0f)
    var azimuthInRadians : Float = 0f


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

                System.arraycopy(
                    event.values,
                    0,
                    magnetometerReading,
                    0,
                    magnetometerReading.size)

                // Berechnung der Rotation Matrix und Orientierung
                val rotationMatrix = FloatArray(9)
                accelerometerReading[0] = AccelerometerListenerSingleton.accelerometerX
                accelerometerReading[1] = AccelerometerListenerSingleton.accelerometerY
                accelerometerReading[2] = AccelerometerListenerSingleton.accelerometerZ

                val orientationAngles = FloatArray(3)
                if (SensorManager.getRotationMatrix(
                        rotationMatrix,
                        null,
                        accelerometerReading,
                        magnetometerReading
                    )
                ) {
                    SensorManager.getOrientation(rotationMatrix, orientationAngles)
                    // orientationAngles[0] enthält die Richtung in Radiant
                    azimuthInRadians = orientationAngles[0]
                    // Umrechnung in Grad und Bildung des gleitenden Mittelwerts
                    orientation =  movingAverage(Math.toDegrees(azimuthInRadians.toDouble()).toFloat())
                    DataSaver.compassList.add(System.currentTimeMillis().toString() + "," + orientation + "\n")

                }

             //TODO   DataSaver.accelerometerList.add(System.currentTimeMillis().toString() + "," + event.values[0] + "," + event.values[1] + "," + event.values[2] + "\n")
            }
        }
    }

}