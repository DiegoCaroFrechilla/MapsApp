package com.example.mapsapp.view

import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import com.example.mapsapp.viewmodel.MapsViewModel

fun DetailScreen(navigationController: NavHostController, myViewModel: MapsViewModel) {

}

@Composable
fun ScaffoldDetailScreen(
    navigationController: NavHostController,
    myViewModel: MapsViewModel,
    state: DrawerState
) {
/*    val selectedMarkerId by myViewModel.markerIDSelected.observeAsState()
    myViewModel.getMarker(selectedMarkerId!!.markerID!!)
    val marker by myViewModel.marker.observeAsState()
    Text(text = "$marker")*/
}