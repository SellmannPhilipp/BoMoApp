package com.praktikum.bomoapp.Singletons

import android.content.Context
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority

object FusedLocationSingleton {
    private var instance: LocationRequest? = null

    fun getInstance(context: Context): LocationRequest {
        if (instance == null) {
            instance = createLocationRequest(context)
        }
        return instance!!
    }

    private fun createLocationRequest(context: Context): LocationRequest {
        val sharedPreferences = context.getSharedPreferences("GPS-Tracking",Context.MODE_PRIVATE)
        val priority : Int = when(sharedPreferences.getInt("priority",1)) {
            1 -> Priority.PRIORITY_HIGH_ACCURACY
            2 -> Priority.PRIORITY_BALANCED_POWER_ACCURACY
            else -> Priority.PRIORITY_HIGH_ACCURACY
        }
        return LocationRequest.Builder(priority,1000).build()

    }
}