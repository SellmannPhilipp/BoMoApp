package com.praktikum.bomoapp.Singletons

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.praktikum.bomoapp.DataSaver
import com.praktikum.bomoapp.viewmodels.LastLocationViewModel
import com.praktikum.bomoapp.viewmodels.MeasurementViewModel
import org.osmdroid.util.GeoPoint

object GpsLocationListenerSingleton {
    private var instance: LocationListener? = null
    var latitude by mutableStateOf(0.0)
    var longitude by mutableStateOf(0.0)

    fun getInstance(context: Context): LocationListener {
        if (instance == null) {
            instance = createLocationListener(context)
        }
        return instance!!
    }

    private fun createLocationListener(context: Context): LocationListener {
        return object : LocationListener {
            override fun onLocationChanged(location: Location) {
                latitude = location.latitude
                longitude = location.longitude
                Log.d("GPS-Tracking", "${location.latitude} ${location.longitude}")
                DataSaver.gpsList.add(System.currentTimeMillis().toString()+","+location.latitude+","+location.longitude+"\n")
                LastLocationViewModel.locationList.add("${location.latitude}" + "," + "${location.longitude}" + "\n")

                if(MeasurementViewModel.measurementActive) {
                    var lastLocation = DataSaver.gpsList.get(DataSaver.gpsList.size - 1)
                    var fragments = lastLocation.split(",")
                    MeasurementViewModel.addGeneralMeasuringPoint(GeoPoint(fragments[1].toDouble(), fragments[2].toDouble()), fragments[0].toLong())
                }
            }
        }
    }
}