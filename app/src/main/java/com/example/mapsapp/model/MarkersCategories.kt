package com.example.mapsapp.model

import androidx.compose.ui.graphics.Color
import com.example.mapsapp.R
import com.example.mapsapp.ui.theme.Jasmine
import com.example.mapsapp.ui.theme.LightRed
import com.example.mapsapp.ui.theme.PersianPink
import com.example.mapsapp.ui.theme.ScreaminGreen
import com.example.mapsapp.ui.theme.TropicalIndigo

sealed class MarkersCategories(val name: String, val color: Color, image: Int) {
    object Home : MarkersCategories("Home", PersianPink, R.drawable.locationlogopink)
    object Shop : MarkersCategories("Shop", TropicalIndigo, R.drawable.locationlogoblue)
    object Restaurants : MarkersCategories("Restaurants", ScreaminGreen, R.drawable.locationlogogreen)
    object Supermarkets : MarkersCategories("Supermarkets", Jasmine, R.drawable.locationlogo)
    object Default: MarkersCategories("Default", LightRed, R.drawable.locationlogored)
}