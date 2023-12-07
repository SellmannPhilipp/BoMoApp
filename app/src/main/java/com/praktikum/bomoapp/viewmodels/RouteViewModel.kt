package com.praktikum.bomoapp.viewmodels

import org.osmdroid.util.GeoPoint

class RouteViewModel {
    companion object {
        private var selectedRoute = 0

        //Route 1
        val polylinePointsOne = listOf(
            GeoPoint(51.44719, 7.27218),
            GeoPoint(51.44688, 7.27248),
            GeoPoint(51.44659, 7.27273),
            GeoPoint(51.44723, 7.27354),
            GeoPoint(51.44676, 7.27326),
            GeoPoint(51.44753, 7.27329),
            GeoPoint(51.4469, 7.27368),
            GeoPoint(51.44736, 7.27277),
            GeoPoint(51.44699, 7.2736),
            GeoPoint(51.4471, 7.2735),
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