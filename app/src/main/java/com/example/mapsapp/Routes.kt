package com.example.mapsapp

sealed class Routes(val routes: String) {
    object MapScreen : Routes("mapScreen")
    object LogScreen: Routes("logScreen")
}