package com.praktikum.bomoapp.viewmodels

import android.util.Log
import org.osmdroid.util.GeoPoint

class RouteViewModel {
    companion object {
        private var selectedRoute = 0
        lateinit var interpolatedPoints: ArrayList<GeoPoint>
        var showInterpolated = false

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

        fun linearInterpolationBetweenPoints(pointA: GeoPoint, pointB: GeoPoint, t1: Int, t2: Int, timeIntervalMillis: Int): List<GeoPoint> {
            val interpolatedPoints = mutableListOf<GeoPoint>()

            val deltaLongitude = pointB.longitude - pointA.longitude
            val deltaLatitude = pointB.latitude - pointA.latitude
            val t21 = t2 - t1

            var t = t1
            while (t < t2) {
                val deltaT = (t - t1).toDouble() / t21.toDouble()
                val newLongitude = pointA.longitude + deltaLongitude * deltaT
                val newLatitude = pointA.latitude + deltaLatitude * deltaT
                val newCoordinate = GeoPoint(newLatitude, newLongitude)
                interpolatedPoints.add(newCoordinate)
                t += timeIntervalMillis
            }

            // Füge den Endpunkt hinzu, um sicherzustellen, dass alle Punkte enthalten sind
            interpolatedPoints.add(pointB)

            return interpolatedPoints
        }
    }
}