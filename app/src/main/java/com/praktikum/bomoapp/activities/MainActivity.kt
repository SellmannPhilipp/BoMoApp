package com.praktikum.bomoapp.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.praktikum.bomoapp.ForegroundService


data class BottomNavigationItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

class MainActivity : ComponentActivity() {
    private var isBound = false
    var serviceIntent: Intent? = null

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            getPermission()
            // Initialisieren Sie die SharedPreferences
            serviceIntent = Intent(LocalContext.current, ForegroundService::class.java)

            val items = listOf(
                BottomNavigationItem(
                    route = "settings",
                    title = "Einstellungen",
                    selectedIcon = Icons.Filled.Settings,
                    unselectedIcon = Icons.Outlined.Settings
                ),
                BottomNavigationItem(
                    route = "map",
                    title = "Karte",
                    selectedIcon = Icons.Filled.Place,
                    unselectedIcon = Icons.Outlined.Place
                ),
                BottomNavigationItem(
                    route = "data",
                    title = "Daten",
                    selectedIcon = Icons.Filled.Done,
                    unselectedIcon = Icons.Outlined.Done
                )
            )

            var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
            val navController = rememberNavController()
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        items.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = selectedItemIndex == index,
                                onClick = {
                                    selectedItemIndex = index
                                    navController.navigate(item.route)
                                },
                                label = {
                                        Text(text = item.title)
                                },
                                icon = {
                                    BadgedBox(badge = {}) {
                                        Icon(imageVector = if(index == selectedItemIndex) {
                                            item.selectedIcon } else item.unselectedIcon,
                                            contentDescription = item.title
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            ) {
                NavHost(navController = navController, startDestination = "settings") {
                    composable("map") {
                        OsmdroidMapView()
                    }
                    composable("settings") {
                        Settings()
                    }
                    composable("data") {
                        Data()
                    }
                }
            }

            //AppContent(intentMap, intentSettings)
        }
    }

    private fun getPermission() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    report.let {

                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(this@MainActivity, "Permissions Granted", Toast.LENGTH_SHORT).show()
                        } else {
                            //Toast.makeText(this@MainActivity, "Please Grant Permissions to use the app", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest?>?, token: PermissionToken?) {
                    token?.continuePermissionRequest()
                }
            }).withErrorListener{
                Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
            }.check()
    }
}
