package com.example.mapsapp.model

data class Marker(
    var userId: String,
    var markerID: String? = null,
    val title: String,
    val latitude: Double,
    val longitude: Double,
    val description: String,
    var image: String?,
    var category: String?
) {
    constructor() : this("", null, "", 0.0, 0.0, "", null, null)
}
