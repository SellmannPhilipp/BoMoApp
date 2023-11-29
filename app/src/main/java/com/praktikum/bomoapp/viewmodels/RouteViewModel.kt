package com.praktikum.bomoapp.viewmodels

import org.osmdroid.util.GeoPoint

class RouteViewModel {
    companion object {
        private var selectedRoute = 0

        //Route 1
        val polylinePointsOne = listOf(
            GeoPoint(51.48122, 7.22009),
            GeoPoint(51.48435, 7.23187),
            GeoPoint(51.49931, 7.19633),
        )

        //Route 2
        val polylinePointsTwo = listOf(
            GeoPoint(51.48122, 7.22009),
            GeoPoint(51.48435, 7.23187),
        )

        fun setSelectedRoute(value: Int) {
            selectedRoute = value
        }

        fun getSelectedRoute(): Int {
            return selectedRoute
        }
    }
}