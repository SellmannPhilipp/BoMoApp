package com.praktikum.bomoapp

import org.osmdroid.util.GeoPoint

class MeasuringPoint {
    private lateinit var location: GeoPoint
    private var timestamp: Long = 0

    constructor(location: GeoPoint, timestamp: Long) {
        this.location = location
        this.timestamp = timestamp
    }

    fun getLocation(): GeoPoint {
        return this.location
    }

    fun getTimestamp(): Long {
        return this.timestamp
    }
}