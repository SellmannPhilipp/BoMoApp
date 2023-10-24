package com.praktikum.bomoapp

import androidx.lifecycle.ViewModel
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.io.File
import java.io.FileOutputStream
import kotlin.concurrent.thread

class DataSaver : ViewModel() {

    companion object {
        val gpsList = mutableListOf("")
        val networkList = mutableListOf("")
        val accelerometerList = mutableListOf("")
        val gyroscopeList = mutableListOf("")
        val lock = Any()

        fun saveAllDataServer(){
            var gpsListCopy: List<String>
            var networkListCopy: List<String>
            var accelerometerListCopy: List<String>
            var gyroscopeListCopy: List<String>
            synchronized(lock) {
                gpsListCopy = gpsList.toList()
                networkListCopy = networkList.toList()
                accelerometerListCopy = accelerometerList.toList()
                gyroscopeListCopy =  gyroscopeList.toList()
                gpsList.clear()
                networkList.clear()
                accelerometerList.clear()
                gyroscopeList.clear()
            }

            //Dateispeicherung Lokal
            thread {
                var outputStream : FileOutputStream
                if (File("/storage/emulated/0/Download/gps.txt").exists()){
                    {}
                } else {
                    outputStream = FileOutputStream("/storage/emulated/0/Download/gps.txt")
                    outputStream.write("Time,Latitude,Longitude\n".toByteArray())
                    outputStream.close()
                }
                if (File("/storage/emulated/0/Download/network.txt").exists()) {

                } else {
                    outputStream = FileOutputStream("/storage/emulated/0/Download/network.txt")
                    outputStream.write("Time,Latitude,Longitude\n".toByteArray())
                    outputStream.close()
                }
                if (File("/storage/emulated/0/Download/acc.txt").exists()) {

                } else {
                    outputStream = FileOutputStream("/storage/emulated/0/Download/acc.txt")
                    outputStream.write("Time,accX,accY,accZ\n".toByteArray())
                    outputStream.close()
                }
                if (File("/storage/emulated/0/Download/gyro.txt").exists()) {

                } else {
                    outputStream = FileOutputStream("/storage/emulated/0/Download/gyro.txt")
                    outputStream.write("Time,gyrX,gyrY,gyrZ\n".toByteArray())
                    outputStream.close()
                }
                outputStream = FileOutputStream("/storage/emulated/0/Download/gps.txt",true)
                outputStream.write(gpsListCopy.joinToString("").toByteArray())
                outputStream.close()
                outputStream = FileOutputStream("/storage/emulated/0/Download/network.txt",true)
                outputStream.write(networkListCopy.joinToString("").toByteArray())
                outputStream.close()
                outputStream = FileOutputStream("/storage/emulated/0/Download/acc.txt",true)
                outputStream.write(accelerometerListCopy.joinToString("").toByteArray())
                outputStream.close()
                outputStream = FileOutputStream("/storage/emulated/0/Download/gyro.txt",true)
                outputStream.write(gyroscopeListCopy.joinToString("").toByteArray())
                outputStream.close()
            }

            //Dateispeicherung Server
            thread {
                val serverUri = "tcp://185.239.238.141:1883"
                val clientId = "AndroidApp"
                val options = MqttConnectOptions()
                options.isCleanSession = true
                val mqttClient = MqttClient(serverUri, clientId, null)
                mqttClient.connect(options)
                var iterator = gpsListCopy.iterator()
                while (iterator.hasNext()) {
                    val element = iterator.next()
                    val message = MqttMessage()
                    message.payload = element.toByteArray()
                    mqttClient.publish("lokalisierung/gps", message)
                }
                iterator = networkListCopy.iterator()
                while (iterator.hasNext()) {
                    val element = iterator.next()
                    val message = MqttMessage()
                    message.payload = element.toByteArray()
                    mqttClient.publish("lokalisierung/network", message)
                }
                iterator = accelerometerListCopy.iterator()
                while (iterator.hasNext()) {
                    val element = iterator.next()
                    val message = MqttMessage()
                    message.payload = element.toByteArray()
                    mqttClient.publish("lokalisierung/acc", message)
                }
                iterator = gyroscopeListCopy.iterator()
                while (iterator.hasNext()) {
                    val element = iterator.next()
                    val message = MqttMessage()
                    message.payload = element.toByteArray()
                    mqttClient.publish("lokalisierung/gyro", message)
                }
                mqttClient.disconnect()
            }
        }
    }
}