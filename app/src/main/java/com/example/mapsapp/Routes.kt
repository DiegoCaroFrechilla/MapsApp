package com.example.mapsapp

sealed class Routes(val routes: String) {
    object MapScreen : Routes("mapScreen")
    object LogScreen : Routes("logScreen")
    object CameraScreen : Routes("cameraScreen")
    object TakePhotoScreen : Routes("takePhotoScreen")
    object GalleryScreen : Routes("galleryScreen")
}