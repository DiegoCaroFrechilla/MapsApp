package com.example.mapsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.theme.MapsAppTheme
import com.example.mapsapp.view.MapScreen
import com.example.mapsapp.view.Mydrawer
import com.example.mapsapp.view.TakePhotoScreen
import com.example.mapsapp.viewmodel.MapsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myViewModel by viewModels<MapsViewModel>()
        setContent {
            MapsAppTheme(darkTheme = true) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navigationController = rememberNavController()
                    Mydrawer(myViewModel)
                    NavHost(
                        navController = navigationController,
                        startDestination = Routes.MapScreen.routes
                    ) {
                        composable(Routes.MapScreen.routes) {
                            MapScreen(
                                navigationController,
                                myViewModel
                            )
                        }
                        composable(Routes.TakePhotoScreen.routes) {
                            TakePhotoScreen(
                                navigationController,
                                myViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

