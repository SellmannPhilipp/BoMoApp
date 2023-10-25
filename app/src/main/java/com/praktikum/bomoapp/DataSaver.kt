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

        fun saveAllData(){
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
                checkForFile("gps.txt")
                checkForFile("network.txt")
                checkForFile("acc.txt")
                checkForFile("gyro.txt")
                writeFile("gps.txt",gpsListCopy)
                writeFile("network.txt",networkListCopy)
                writeFile("acc.txt",accelerometerListCopy)
                writeFile("gyro.txt",gyroscopeListCopy)
            }

            //Dateispeicherung Server
            thread {
                val serverUri = "tcp://185.239.238.141:1883"
                val clientId = "AndroidApp"
                val options = MqttConnectOptions()
                options.isCleanSession = true
                val mqttClient = MqttClient(serverUri, clientId, null)
                mqttClient.connect(options)
                sendToServer(mqttClient,"gps",gpsListCopy)
                sendToServer(mqttClient,"network",networkListCopy)
                sendToServer(mqttClient,"acc",accelerometerListCopy)
                sendToServer(mqttClient,"gyro",gyroscopeListCopy)
                mqttClient.disconnect()
            }
        }

        private fun sendToServer(mqttClient: MqttClient, topic: String, list: List<String>) {
            val iterator = list.iterator()
            while (iterator.hasNext()) {
                val element = iterator.next()
                val message = MqttMessage()
                message.payload = element.toByteArray()
                mqttClient.publish("lokalisierung/"+topic, message)
            }

        }

        private fun writeFile(dateiName: String,liste:  List<String>) {
            val outputStream = FileOutputStream("/storage/emulated/0/Download/"+dateiName,true)
            outputStream.write(liste.joinToString("").toByteArray())
            outputStream.close()
        }

        private fun checkForFile(dateiName: String) {

            if (!File("/storage/emulated/0/Download/"+dateiName).exists()) {
                val outputStream = FileOutputStream("/storage/emulated/0/Download/"+dateiName)
                outputStream.write("Time,Latitude,Longitude\n".toByteArray())
                outputStream.close()
            }

        }
    }
}