import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import com.praktikum.bomoapp.Singletons.NetworkLocationListenerSingleton


class NetworkTrackingViewModel(context: Context) : ViewModel() {

    private var locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var locationListener: LocationListener? = null
    private var ctx = context

    var latitude by mutableStateOf(0.0)
        private set

    var longitude by mutableStateOf(0.0)
        private set

    var tracking by mutableStateOf(false)

    fun onLocationChanged(location: Location) {
        latitude = location.latitude
        longitude = location.longitude
    }

    init {
        val sharedPreferences = context.getSharedPreferences("NetworkTracking", Context.MODE_PRIVATE)

        // Wert von 'tracking' aus den SharedPreferences wiederherstellen (mit einem Standardwert von 'false')
        tracking = sharedPreferences.getBoolean("tracking", false)
    }

    @SuppressLint("MissingPermission")
    fun start() {
        val locationListener = NetworkLocationListenerSingleton.getInstance(ctx)

        if (tracking) {
            locationListener?.let {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 0f, it)
            }
        }
    }

    fun stop() {
        val locationListener = NetworkLocationListenerSingleton.getInstance(ctx)
        locationListener?.let { locationManager.removeUpdates(it) }
    }

    fun toggleTracking() {
        tracking = !tracking

        // SharedPreferences-Instanz abrufen
        val sharedPreferences = ctx.getSharedPreferences("NetworkTracking", Context.MODE_PRIVATE)

        // Editor zum Bearbeiten der SharedPreferences
        val editor = sharedPreferences.edit()

        // Den Wert von 'tracking' speichern
        editor.putBoolean("tracking", tracking)

        // Änderungen speichern
        editor.apply()

        if (tracking) {
            start()
        } else {
            stop()
        }
    }
}