package com.example.mapsapp.model

import com.example.mapsapp.Routes

sealed class DrawerItems(val route: String, val label: String) {
    object Map : DrawerItems(Routes.MapScreen.routes, "Map")
    object SavedMarkers : DrawerItems(Routes.SavedMarkersScreen.routes, "Saved Markers")
}