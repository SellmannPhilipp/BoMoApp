package com.praktikum.bomoapp.viewmodels

import android.util.Log
import com.praktikum.bomoapp.MeasuringPoint
import org.osmdroid.util.GeoPoint

class RouteViewModel {
    companion object {
        private var selectedRoute = 0
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

        fun linearInterpolationBetweenPoints(pointA: GeoPoint, pointB: GeoPoint, t1: Long, t2: Long, timeIntervalMillis: Int): List<MeasuringPoint> {
            val interpolatedPoints = mutableListOf<MeasuringPoint>()

            val deltaLongitude = pointB.longitude - pointA.longitude
            val deltaLatitude = pointB.latitude - pointA.latitude
            val t21 = t2 - t1

            var t = t1
            while (t < t2) {
                val deltaT = (t - t1).toDouble() / t21.toDouble()
                val newLongitude = pointA.longitude + deltaLongitude * deltaT
                val newLatitude = pointA.latitude + deltaLatitude * deltaT
                val newCoordinate = GeoPoint(newLatitude, newLongitude)
                interpolatedPoints.add(MeasuringPoint(newCoordinate, t))
                t += timeIntervalMillis
            }

            // Füge den Endpunkt hinzu, um sicherzustellen, dass alle Punkte enthalten sind
            interpolatedPoints.add(MeasuringPoint(pointB, t))

            return interpolatedPoints
        }

        fun interpolateRoute(points: List<GeoPoint>): List<MeasuringPoint> {
            val interpolatedPoints = mutableListOf<MeasuringPoint>()
            var length = points.size - 1
            var i = 0

            while (i < length) {
                if (MeasurementViewModel.userTrackedMeasuringPoints.size > i + 1) {
                    var pointA = points[i]
                    var pointB = points[i + 1]
                    var t1 = MeasurementViewModel.userTrackedMeasuringPoints[i].getTimestamp()
                    var t2 = MeasurementViewModel.userTrackedMeasuringPoints[i + 1].getTimestamp()
                    var timeInMillis = 1000
                    interpolatedPoints.addAll(linearInterpolationBetweenPoints(pointA, pointB, t1, t2, timeInMillis))
                    i += 1
                } else {
                    Log.d("Problem", "Problem")
                    break
                }
            }

            return interpolatedPoints
        }
    }
}