package com.example.mapsapp

sealed class Routes(val routes: String) {
    object MapScreen : Routes("mapScreen")
    object LoginScreen : Routes("loginScreen")
    object CameraScreen : Routes("cameraScreen")
    object TakePhotoScreen : Routes("takePhotoScreen")
    object GalleryScreen : Routes("galleryScreen")
    object SavedMarkersScreen : Routes("savedMarkersScreen")
}