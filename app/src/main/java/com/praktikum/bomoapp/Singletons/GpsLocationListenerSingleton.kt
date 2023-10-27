package com.praktikum.bomoapp.Singletons

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.util.Log
import com.praktikum.bomoapp.DataSaver

object GpsLocationListenerSingleton {
    private var instance: LocationListener? = null

    fun getInstance(context: Context): LocationListener {
        if (instance == null) {
            instance = createLocationListener(context)
        }
        return instance!!
    }

    private fun createLocationListener(context: Context): LocationListener {
        return object : LocationListener {
            override fun onLocationChanged(location: Location) {
                Log.d("GPS-Tracking", "${location.latitude} ${location.longitude}")
                DataSaver.gpsList.add(System.currentTimeMillis().toString()+","+location.latitude+","+location.longitude+"\n")
            }
        }
    }
}