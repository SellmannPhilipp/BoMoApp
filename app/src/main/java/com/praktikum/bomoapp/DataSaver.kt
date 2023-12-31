package com.praktikum.bomoapp

import android.content.Context
import android.widget.Toast
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
        val compassList = mutableListOf("")
        val lock = Any()

        fun saveAllData(context: Context){
            var gpsListCopy: List<String>
            var networkListCopy: List<String>
            var accelerometerListCopy: List<String>
            var gyroscopeListCopy: List<String>
            var compassListCopy: List<String>
            synchronized(lock) {
                gpsListCopy = gpsList.toList()
                networkListCopy = networkList.toList()
                accelerometerListCopy = accelerometerList.toList()
                gyroscopeListCopy =  gyroscopeList.toList()
                compassListCopy =  compassList.toList()
                gpsList.clear()
                networkList.clear()
                accelerometerList.clear()
                gyroscopeList.clear()
                compassList.clear()
            }
            val anzahlElemente = gpsListCopy.size + networkListCopy.size + accelerometerListCopy.size + gyroscopeListCopy.size + compassListCopy.size
            val toast = Toast(context)
            toast.setText("$anzahlElemente Daten werden gespeichert")
            toast.show()
            //Dateispeicherung Lokal
            thread {
                checkForFile()
                writeFile("gps.txt",gpsListCopy)
                writeFile("network.txt",networkListCopy)
                writeFile("acc.txt",accelerometerListCopy)
                writeFile("gyro.txt",gyroscopeListCopy)
                writeFile("compass.txt",compassListCopy)
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
                sendToServer(mqttClient,"compass",compassListCopy)
                mqttClient.disconnect()
                Thread.sleep(2_000)
                toast.cancel()
                toast.setText("Fertig")
                toast.show()
            }
        }

        private fun sendToServer(mqttClient: MqttClient, topic: String, list: List<String>) {
            val message = MqttMessage()
            //Aufteilen der Liste falls zu groß
            if (list.size > 30000) {
                var listToSend = list.slice(0..29999)
                var restList = list.slice(30000..(list.size-1))
                message.payload = listToSend.joinToString("").toByteArray()
                mqttClient.publish("lokalisierung/" + topic, message)
                while (restList.size>30000) {
                    listToSend = restList.slice(0..29999)
                    restList = restList.slice(30000..(restList.size-1))
                    message.payload = listToSend.joinToString("").toByteArray()
                    mqttClient.publish("lokalisierung/" + topic, message)
                }
                message.payload = restList.joinToString("").toByteArray()
                mqttClient.publish("lokalisierung/" + topic, message)
            } else {
                message.payload = list.joinToString("").toByteArray()
                mqttClient.publish("lokalisierung/" + topic, message)
            }

        }

        private fun writeFile(dateiName: String,liste:  List<String>) {
            val outputStream = FileOutputStream("/storage/emulated/0/Download/"+dateiName,true)
            outputStream.write(liste.joinToString("").toByteArray())
            outputStream.close()
        }

        private fun checkForFile() {

            if (!File("/storage/emulated/0/Download/gps.txt").exists()) {
                val outputStream = FileOutputStream("/storage/emulated/0/Download/gps.txt")
                outputStream.write("Time,Latitude,Longitude\n".toByteArray())
                outputStream.close()
            }
            if (!File("/storage/emulated/0/Download/network.txt").exists()) {
                val outputStream = FileOutputStream("/storage/emulated/0/Download/network.txt")
                outputStream.write("Time,Latitude,Longitude\n".toByteArray())
                outputStream.close()
            }
            if (!File("/storage/emulated/0/Download/acc.txt").exists()) {
                val outputStream = FileOutputStream("/storage/emulated/0/Download/acc.txt")
                outputStream.write("Time,accX,accY,accZ\n".toByteArray())
                outputStream.close()
            }
            if (!File("/storage/emulated/0/Download/gyro.txt").exists()) {
                val outputStream = FileOutputStream("/storage/emulated/0/Download/gyro.txt")
                outputStream.write("Time,gyrX,gyrY,gyrZ\n".toByteArray())
                outputStream.close()
            }
            if (!File("/storage/emulated/0/Download/compass.txt").exists()) {
                val outputStream = FileOutputStream("/storage/emulated/0/Download/compass.txt")
                outputStream.write("Time,orientation,magX,magY,magZ\n".toByteArray())
                outputStream.close()
            }


        }
    }
}