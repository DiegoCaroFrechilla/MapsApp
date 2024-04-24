package com.example.mapsapp.model

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class Repository {
    private val database = FirebaseFirestore.getInstance()

    fun addMarker(marker: Marker) {
        database.collection("markers")
            .add(
                hashMapOf(
                    "user ID" to marker.userId,
                    "title" to marker.title,
                    "latitude" to marker.latitude,
                    "longitude" to marker.longitude,
                    "description" to marker.description,
                    "image" to marker.image,
                    "category" to marker.category
                )
            )
    }

    fun editMarker(editedMarker: Marker) {
        database.collection("markers").document(editedMarker.markerID!!).set(
            hashMapOf(
                "title" to editedMarker.title,
                "latitude" to editedMarker.latitude,
                "longitude" to editedMarker.longitude,
                "description" to editedMarker.description,
                "image" to editedMarker.image,
                "category" to editedMarker.category
            )
        )
    }

    fun deleteMarker(markerID: String) {
        database.collection("markers").document(markerID).delete()
    }

    fun getMarkers(): CollectionReference {
        return database.collection("markers")
    }

    fun getMarker(markerID: String): DocumentReference {
        return database.collection("markers").document(markerID)
    }
}