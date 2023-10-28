
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AccelerometerViewModel(context: Context) : ViewModel() {
    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var sensorEventListener: SensorEventListener? = null
    private var ctx = context

    var tracking by mutableStateOf(false)

    init {
        val sharedPreferences = context.getSharedPreferences("Accelerometer", Context.MODE_PRIVATE)

        // Wert von 'tracking' aus den SharedPreferences wiederherstellen (mit einem Standardwert von 'false')
        tracking = sharedPreferences.getBoolean("tracking", false)
    }

    fun start(samplingRate: Int) {
        this.sensorEventListener  = AccelerometerListenerSingleton.getInstance(ctx)
        viewModelScope.launch {
            sensorManager.registerListener(
                sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                samplingRate,
            )
        }
    }

    fun stop() {
        this.sensorEventListener  = AccelerometerListenerSingleton.getInstance(ctx)
        sensorEventListener?.let {
            sensorManager.unregisterListener(it)
        }
    }

    fun toggleAccelerometer(samplingRate: Int) {
        tracking = !tracking

        // SharedPreferences-Instanz abrufen
        val sharedPreferences = ctx.getSharedPreferences("Accelerometer", Context.MODE_PRIVATE)

        // Editor zum Bearbeiten der SharedPreferences
        val editor = sharedPreferences.edit()

        // Den Wert von 'tracking' speichern
        editor.putBoolean("tracking", tracking)

        // Änderungen speichern
        editor.apply()

        if (tracking) {
            start(samplingRate)
        } else {
            stop()
        }
    }
}