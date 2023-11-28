package com.praktikum.bomoapp.Singletons

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.praktikum.bomoapp.DataSaver

object LocationCallbackSingelton {
    private var instance: LocationCallback? = null
    var latitude by mutableStateOf(0.0)
    var longitude by mutableStateOf(0.0)

    fun getInstance(context: Context): LocationCallback {
        if (instance == null) {
            instance = createLocationCallback(context)
        }
        return instance!!
    }

    private fun createLocationCallback(context: Context): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                if(p0.lastLocation != null) {
                        latitude = p0.lastLocation!!.latitude
                        longitude = p0.lastLocation!!.longitude
                        DataSaver.fusedList.add(System.currentTimeMillis().toString()+","+latitude+","+longitude+"\n")
                        Log.d("Fused", "$latitude $longitude")
                    }
            }
        }
    }
}