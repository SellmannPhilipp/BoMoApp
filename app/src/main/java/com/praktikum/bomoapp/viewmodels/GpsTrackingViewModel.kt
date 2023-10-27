import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import com.praktikum.bomoapp.Singletons.GpsLocationListenerSingleton


class GpsTrackingViewModel(context: Context) : ViewModel() {

    private var locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var locationListener: LocationListener? = null
    private var ctx = context

    var tracking by mutableStateOf(false)

    init {
        val sharedPreferences = context.getSharedPreferences("GPS-Tracking", Context.MODE_PRIVATE)

        // Wert von 'tracking' aus den SharedPreferences wiederherstellen (mit einem Standardwert von 'false')
        tracking = sharedPreferences.getBoolean("tracking", false)
    }

    @SuppressLint("MissingPermission")
    fun start() {
        this.locationListener = GpsLocationListenerSingleton.getInstance(ctx)

        if (tracking) {
            locationListener?.let {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 0f, it)
            }
        }
    }

    fun stop() {
        this.locationListener = GpsLocationListenerSingleton.getInstance(ctx)
        locationListener?.let { locationManager.removeUpdates(it) }
    }

    fun toggleTracking() {
        tracking = !tracking

        // SharedPreferences-Instanz abrufen
        val sharedPreferences = ctx.getSharedPreferences("GPS-Tracking", Context.MODE_PRIVATE)

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