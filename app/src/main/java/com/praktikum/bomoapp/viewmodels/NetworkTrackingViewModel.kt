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
import android.util.Log
import androidx.compose.runtime.remember


class NetworkTrackingViewModel(context: Context) : ViewModel() {
    private var locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var locationListener: LocationListener? = null

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
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                Log.d("Network-Tracking", "${location.latitude} ${location.longitude}")
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun toggleTracking() {
        tracking = !tracking

        if (tracking) {
            locationListener?.let {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 0f, it)
            }
        } else {
            locationListener?.let { locationManager.removeUpdates(it) }
        }
    }
}

