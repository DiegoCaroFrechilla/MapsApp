package com.example.mapsapp.viewmodel

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp.R
import com.google.android.gms.maps.model.LatLng

class MapsViewModel : ViewModel() {
    //Marker
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

    //Camera
    private val _cameraPermissionGranted = MutableLiveData(false)
    val cameraPermissionGranted = _cameraPermissionGranted

    private val _shouldShowPermissionRationale = MutableLiveData(false)
    val shouldShowPermissionRationale = _shouldShowPermissionRationale

    private val _showPermissionDenied = MutableLiveData(false)
    val showPermissionDenied = _showPermissionDenied

    fun setCameraPermissionGranted(granted: Boolean) {
        _cameraPermissionGranted.value = granted
    }

    fun setShouldShowPermissionRationale(should: Boolean) {
        _shouldShowPermissionRationale.value = should
    }

    fun setShowPermissionDenied(denied: Boolean) {
        _showPermissionDenied.value = denied
    }

}

//TODO Cambiar nombre e importar fuentes
val lemonMilkBold = FontFamily(Font(R.font.lemon_milk_bold))
val lemonMilkBoldItalic = FontFamily(Font(R.font.lemon_milk_bold_italic))
val lemonMilkLight = FontFamily(Font(R.font.lemon_milk_light))
val lemonMilkLightItalic = FontFamily(Font(R.font.lemon_milk_light_italic))
val lemonMilkMedium = FontFamily(Font(R.font.lemon_milk_medium))
val lemonMilkMediumItalic = FontFamily(Font(R.font.lemon_milk_medium_italic))
val lemonMilkRegular = FontFamily(Font(R.font.lemon_milk_regular))
val lemonMilkRegularItalic = FontFamily(Font(R.font.lemon_milk_regular_italic))
