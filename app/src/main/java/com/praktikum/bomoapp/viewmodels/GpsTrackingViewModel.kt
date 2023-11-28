
import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationServices
import com.praktikum.bomoapp.Singletons.FusedLocationSingleton
import com.praktikum.bomoapp.Singletons.GpsLocationListenerSingleton
import com.praktikum.bomoapp.Singletons.LocationCallbackSingelton

class GpsTrackingViewModel(context: Context) : ViewModel() {

    private var locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var locationListener: LocationListener? = null
    private var ctx = context
    private var fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ctx)



    var tracking by mutableStateOf(false)
    var sharedPriority by mutableStateOf(1)

    init {
        val sharedPreferences = context.getSharedPreferences("GPS-Tracking", Context.MODE_PRIVATE)

        // Wert von 'tracking' aus den SharedPreferences wiederherstellen (mit einem Standardwert von 'false')
        tracking = sharedPreferences.getBoolean("tracking", false)
        sharedPriority = sharedPreferences.getInt("priority",1)
    }

    @SuppressLint("MissingPermission")
    fun start() {
        this.locationListener = GpsLocationListenerSingleton.getInstance(ctx)
        var locationCallback = LocationCallbackSingelton.getInstance(ctx)
        var locationRequest = FusedLocationSingleton.getInstance(ctx)
        Log.d("Tracking","Instance erstellt")

        if (tracking) {
            when(sharedPriority) {
                1,2 -> fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,Looper.getMainLooper())
                3 -> locationListener?.let {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 0f, it)
                }
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