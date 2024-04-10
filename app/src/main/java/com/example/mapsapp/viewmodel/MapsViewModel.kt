package com.example.mapsapp.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp.R
import com.example.mapsapp.Routes
import com.example.mapsapp.model.Repository
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MapsViewModel : ViewModel() {
    //Marker
    data class Marker(
        var markerID: String? = null,
        val title: String,
        val latitude: Double,
        val longitude: Double,
        val description: String,
        var image: Bitmap?
    ) {
        constructor() : this(null, "", 0.0, 0.0, "", null)
    }

    private val _markers = MutableLiveData<MutableList<Marker>>(mutableListOf())
    val markers = _markers
    private val _showBottom = MutableLiveData(false)
    val showBottom = _showBottom

    private val _geolocation = MutableLiveData(LatLng(0.0, 0.0))
    val geolocation = _geolocation

    private val _markerImage = MutableLiveData<Bitmap>()
    val markerImage = _markerImage
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
                    markerID = null,
                    title = locationName,
                    latitude = _geolocation.value!!.latitude,
                    longitude = _geolocation.value!!.longitude,
                    description = locationDescription,
                    image = _markerImage.value
                )
            }
        if (newMarker != null) {
            currentMarkers.add(newMarker)
            Repository().addMarker(newMarker)
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

    //FIRESTORE

    val repository = Repository()
    fun getMarkers() {
        repository.getMarkers().addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("Firestore error", error.message.toString())
                return@addSnapshotListener
            }
            val tempList = mutableListOf<Marker>()
            for (dc: DocumentChange in value?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {
                    val newMarker = dc.document.toObject(Marker::class.java)
                    newMarker.markerID = dc.document.id
                    tempList.add(newMarker)
                }
            }
            _markers.value = tempList
        }
    }

    fun getMarker(markerID: String) {
        repository.getMarker(markerID).addSnapshotListener { value, error ->
            if (error != null) {
                Log.w("UserRepository", "Listen failed", error)
                return@addSnapshotListener
            }
            if (value != null && value.exists()) {
                val marker = value.toObject(Marker::class.java)
                if (marker != null) {
                    marker.markerID = markerID
                }
            } else {
                Log.d("UserRepository", "Current data: null")
            }
        }
    }

    fun uploadImage(imageUri: Uri?) {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storage = FirebaseStorage.getInstance().getReference("images/$fileName")
        if (imageUri != null) {
            storage.putFile(imageUri)
                .addOnSuccessListener {
                    Log.i("IMAGE UPLOAD", "Image upload successfully")
                    storage.downloadUrl.addOnSuccessListener {
                        Log.i("IMAGE", it.toString())
                    }
                }
                .addOnFailureListener {
                    Log.i("IMAGE UPLOAD", "Image upload failed")
                }
        }
    }

    //Authentication

    private val auth = FirebaseAuth.getInstance()

    private val _goToNext = MutableLiveData(false)
    val goToNext = _goToNext

    private val _userId = MutableLiveData<String>()
    val userId = _userId

    private val _loggedUser = MutableLiveData<String>()
    val loggedUser = _loggedUser

    fun register(username: String, password: String) {
        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _goToNext.value = true
                } else {
                    _goToNext.value = false
                    Log.d("Error", "Error creating user: ${task.result}")
                }
//                modifyProcessing()
            }
    }


    fun login(username: String?, password: String?) {
        auth.signInWithEmailAndPassword(username!!, password!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _userId.value = task.result.user?.uid
                    _loggedUser.value = task.result.user?.email?.split("@")?.get(0)
                    _goToNext.value = true
                } else {
                    _goToNext.value = false
                    Log.d("Error", "Error signing in ${task.result}")
                }
//                modifyProcessing()
            }
    }

/*    private fun modifyProcessing(): AuthResult? {
//TODO Circle progress bar
    }*/

    fun logout() {
        auth.signOut()
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
