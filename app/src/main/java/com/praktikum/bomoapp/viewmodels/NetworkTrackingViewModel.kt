package com.praktikum.bomoapp.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager


class NetworkTrackingViewModel(context: Context) : ViewModel() {
    private var locationManager: LocationManager? = null
    private val locationListener: LocationListener? = null

    var latitude by mutableStateOf(0.0)
        private set

    var longitude by mutableStateOf(0.0)
        private set

    var tracking by mutableStateOf(false)
        private set

    fun onLocationChanged(location: Location) {
        latitude = location.latitude
        longitude = location.longitude
    }

    init {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }


    fun toggleTracking() {
        tracking = !tracking
    }

    @SuppressLint("MissingPermission")
    fun startTracking(locationListener: LocationListener) {
        if (locationManager != null) {
            locationManager!!.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000L, 0f, locationListener
            )
        }
    }

    fun stopTracking(locationListener: LocationListener) {
        locationManager?.removeUpdates(locationListener)
    }
}