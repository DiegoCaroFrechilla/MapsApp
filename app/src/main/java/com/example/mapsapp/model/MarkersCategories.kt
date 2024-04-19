package com.example.mapsapp.model

sealed class MarkersCategories(val name: String, val color: Colors) {
    object Home : MarkersCategories("Home", Colors.Magenta)
    object Shopping : MarkersCategories("Shopping", Colors.Blue)
    object Restaurants : MarkersCategories("Restaurants", Colors.Green)
    object Supermarkets : MarkersCategories("Supermarkets", Colors.Yellow)
}