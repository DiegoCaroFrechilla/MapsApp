package com.example.mapsapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MapsViewModel : ViewModel() {

    data class Marker(
        val state: LatLng,
        val title: String,
        val description: String,
    )

    private val _markers = MutableLiveData<MutableList<Marker>>(mutableListOf())
    val markers = _markers
    private val _showBottom = MutableLiveData(false)
    val showBottom = _showBottom

    private val _geolocation = MutableLiveData(LatLng(0.0, 0.0))
    val geolocation = _geolocation

    fun showBottomSheet(latLng: LatLng) {
        _showBottom.value = true
        _geolocation.value = latLng
    }

    fun hideBottomSheet() {
        _showBottom.value = false
    }

    //TODO Elegir color marcador
    fun CreateMarker(locationName: String, locationDescription: String) {
        val currentMarkers = _markers.value ?: mutableListOf()
        val newMarker =
            geolocation.value?.let {
                Marker(
                    state = it,
                    title = locationName,
                    description = locationDescription
                )
            }
        if (newMarker != null) {
            currentMarkers.add(newMarker)
        }
        _markers.value = currentMarkers
        hideBottomSheet()
    }
}