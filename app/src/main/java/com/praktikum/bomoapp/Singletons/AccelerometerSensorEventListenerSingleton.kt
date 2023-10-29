import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.praktikum.bomoapp.DataSaver


var accXHistory = FloatArray(10)
fun createHistory(newValue: Float): MutableList<Float> {


    for (i in 0 until (accXHistory.size - 1)) {
        accXHistory[i] = accXHistory[i + 1]
    }
    accXHistory[accXHistory.size - 1] = newValue

    var temp = accXHistory.toList()
    return temp.toMutableList()
}


object AccelerometerListenerSingleton {
    private var instance: SensorEventListener? = null

     var test = mutableListOf<Float>();

    var accelerometerX by mutableStateOf(0f)
    var accelerometerY by mutableStateOf(0f)
    var accelerometerZ by mutableStateOf(0f)


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
                accelerometerX = event.values[0]
                test = createHistory(accelerometerX)
                accelerometerY = event.values[1]
                accelerometerZ = event.values[2]
                Log.d("Accelerometer", "${event.values[0]}\n${event.values[1]}\n${event.values[2]}")
                DataSaver.accelerometerList.add(System.currentTimeMillis().toString() + "," + event.values[0] + "," + event.values[1] + "," + event.values[2] + "\n")
            }
        }
    }
}