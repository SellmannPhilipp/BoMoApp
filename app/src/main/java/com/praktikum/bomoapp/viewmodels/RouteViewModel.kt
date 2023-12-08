package com.praktikum.bomoapp.viewmodels

import org.osmdroid.util.GeoPoint

class RouteViewModel {
    companion object {
        private var selectedRoute = 0

        //Route 1
        val polylinePointsOne = listOf(
            GeoPoint(51.4465, 7.2719),
            GeoPoint(51.44641, 7.27196),
            GeoPoint(51.44629, 7.27206),
            GeoPoint(51.44646, 7.27227),
            GeoPoint(51.44661, 7.2727),
            GeoPoint(51.44687, 7.27246),
            GeoPoint(51.44718, 7.2722),
            GeoPoint(51.44728, 7.27252),
            GeoPoint(51.44741, 7.27293),
            GeoPoint(51.44751, 7.27329),
        )

        //Route 2
        val polylinePointsTwo = listOf(
            GeoPoint(51.44798, 7.27083),
            GeoPoint(51.44818, 7.27137),
            GeoPoint(51.44829, 7.27171),
            GeoPoint(51.4481, 7.27186),
            GeoPoint(51.44794, 7.27201),
            GeoPoint(51.44789, 7.27198),
            GeoPoint(51.44772, 7.27213),
            GeoPoint(51.44752, 7.27229),
            GeoPoint(51.44728, 7.27253),
            GeoPoint(51.44718, 7.2722),
            GeoPoint(51.44707, 7.27178),
        )

        fun setSelectedRoute(value: Int) {
            selectedRoute = value
        }

        fun getSelectedRoute(): Int {
            return selectedRoute
        }
    }
}