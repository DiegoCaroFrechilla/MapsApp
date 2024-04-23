package com.example.mapsapp.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp.R
import com.example.mapsapp.model.MarkersCategories
import com.example.mapsapp.model.Repository
//import com.example.mapsapp.model.UserPrefs
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

    private val _locationName = MutableLiveData("")
    val locationName = _locationName

    private val _locationDescription = MutableLiveData("")
    val locationDescription = _locationDescription

    private val _category = MutableLiveData("")
    val category = _category

    private val _markers = MutableLiveData<MutableList<Marker>>(mutableListOf())
    val markers = _markers

    private val _marker = MutableLiveData<Marker>()
    val marker = _marker

    private val _showBottom = MutableLiveData(false)
    val showBottom = _showBottom

    private val _geolocation = MutableLiveData(LatLng(0.0, 0.0))
    val geolocation = _geolocation

    private val _markerImage = MutableLiveData<Bitmap>()
    val markerImage = _markerImage

    private val _newMarker: Marker = Marker()
    var newMarker = _newMarker

    private val _markerIDSelected = MutableLiveData<Marker>()
    val markerIDSelected = _markerIDSelected

    fun selectMarker(markerID: Marker) {
        _markerIDSelected.value = markerID
    }

    private val _categories = MutableLiveData(
        listOf(
            MarkersCategories.Home,
            MarkersCategories.Shop,
            MarkersCategories.Restaurants,
            MarkersCategories.Supermarkets,
        )
    )

    val categories = _categories
    fun changeTitle(title: String) {
        _locationName.value = title
    }

    fun changeDescription(description: String) {
        _locationDescription.value = description
    }

    fun changeCategory(category: String) {
        _category.value = category
    }

    fun showBottomSheet(latLng: LatLng) {
        _showBottom.value = true
        _geolocation.value = latLng
    }

    fun hideBottomSheet() {
        _showBottom.value = false
    }

    fun CreateMarker(
        locationName: String,
        locationDescription: String,
        image: String?,
        category: String?
    ) {
        val currentMarkers = _markers.value ?: mutableListOf()
        newMarker =
            geolocation.value?.let {
                Marker(
                    userId.value!!,
                    markerID = null,
                    title = locationName,
                    latitude = _geolocation.value!!.latitude,
                    longitude = _geolocation.value!!.longitude,
                    description = locationDescription,
                    image = image,
                    category = category
                )
            }!!
        if (newMarker != null) {
            currentMarkers.add(newMarker)
            Repository().addMarker(newMarker)
        }
        _markers.value = currentMarkers
        getMarkers()
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
        repository.getMarkers().whereEqualTo("user ID", userId.value)
            .addSnapshotListener { value, error ->
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
            var tempMarker = Marker()
            if (value != null && value.exists()) {
                val marker1 = value.toObject(Marker::class.java)
                if (marker1 != null) {
                    marker1.markerID = markerID
                    tempMarker = marker1
                }
                _marker.value = tempMarker
            } else {
                Log.d("UserRepository", "Current data: null")
            }
        }
    }

    fun getMarkersFiltered(category: String?) {
        repository.getMarkers().whereEqualTo("user ID", userId.value)
            .whereEqualTo("category", category)
            .addSnapshotListener { value, error ->
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

    private val _image = MutableLiveData<Bitmap?>()
    val image = _image

    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri = _imageUri

    fun choosenImage(bitmap: Bitmap, uri: Uri?) {
        image.value = bitmap
        _imageUri.value = uri
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
                        CreateMarker(
                            locationName.value!!,
                            locationDescription.value!!,
                            it.toString(),
                            category.value

                        )
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
        _goToNext.value = false
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

val coolveticaCompressed = FontFamily(Font(R.font.coolvetica_compressed_hv))
val coolveticaCondensed = FontFamily(Font(R.font.coolvetica_condensed_rg))
val coolveticaCrammed = FontFamily(Font(R.font.coolvetica_crammed_rg))
val coolveticaRg = FontFamily(Font(R.font.coolvetica_rg))
val coolveticaRgIt = FontFamily(Font(R.font.coolvetica_rg_it))


/*
* TODO
* Hacer Log In/Out ✅
* Hacer Fotos
* Modificar eliminar marcadores
* Filtrar marcadores ✅
* Hacer pagina detall del marcador
* Revisar Estilo paginas
* 6
* Control de errores log in (contraseña mal) (usuario no existente)
*
* Register Screen
* Asegurarse que el eemail tiene @ y .
* Que la contraseña tiene al menos 6 caracteres
*
* Login Screen
* Que el usuario exista
*
* */