package com.example.mapsapp

import LoginScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.theme.MapsAppTheme
import com.example.mapsapp.view.GalleryScreen
import com.example.mapsapp.view.MapScreen
import com.example.mapsapp.view.Mydrawer
import com.example.mapsapp.view.RegisterScreen
import com.example.mapsapp.view.SavedMarkersScreen
import com.example.mapsapp.view.TakePhotoScreen
import com.example.mapsapp.viewmodel.MapsViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
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
                    Mydrawer(myViewModel, navigationController)
                    NavHost(
                        navController = navigationController,
                        startDestination = Routes.LoginScreen.routes
                    ) {
                        composable(Routes.LoginScreen.routes) {
                            LoginScreen(navigationController, myViewModel)
                        }
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
                        composable(Routes.GalleryScreen.routes) {
                            GalleryScreen(
                                navigationController,
                                myViewModel
                            )
                        }
                        composable(Routes.SavedMarkersScreen.routes) {
                            SavedMarkersScreen(
                                navigationController,
                                myViewModel
                            )
                        }
                        composable(Routes.RegisterScreen.routes){
                            RegisterScreen(
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

