package com.praktikum.bomoapp.viewmodels

import androidx.lifecycle.ViewModel
import org.osmdroid.util.GeoPoint

class LastLocationViewModel: ViewModel() {
    companion object {
        var geoPoint: GeoPoint? = null
        val locationList = mutableListOf("")

        private fun getLastLocationString(): String {
            return locationList.get(locationList.size - 1)
        }

        private fun extractGeoPoint(s: String): GeoPoint {
            val parts = s.split(",")
            return GeoPoint(parts[0].toDouble(), parts[1].toDouble())
        }

        fun getLastLocation() {
            this.geoPoint = extractGeoPoint(getLastLocationString())
        }
    }
}