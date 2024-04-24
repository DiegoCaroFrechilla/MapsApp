package com.example.mapsapp.view

import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mapsapp.R
import com.example.mapsapp.Routes
import com.example.mapsapp.ui.theme.CoolGray2
import com.example.mapsapp.ui.theme.Jasmine
import com.example.mapsapp.viewmodel.MapsViewModel
import com.example.mapsapp.viewmodel.coolveticaRgIt
import com.example.mapsapp.viewmodel.lemonMilkBoldItalic
import com.example.mapsapp.viewmodel.lemonMilkMediumItalic
import com.google.accompanist.permissions.ExperimentalPermissionsApi

fun DetailScreen(navigationController: NavHostController, myViewModel: MapsViewModel) {

}

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class,
    ExperimentalGlideComposeApi::class
)
@Composable
fun ScaffoldDetailScreen(
    navigationController: NavHostController,
    myViewModel: MapsViewModel,
    state: DrawerState
) {
    val selectedMarkerId by myViewModel.markerIDSelected.observeAsState()
    myViewModel.getMarker(selectedMarkerId!!.markerID!!)
    val marker by myViewModel.marker.observeAsState()
    val categories by myViewModel.categories.observeAsState()
    val showAlert by myViewModel.showDialog.observeAsState()
    Scaffold(
        topBar = { DetailScreenTopAppBar(myViewModel, state, navigationController) }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            if (showAlert == true) {
                DeleteMarkerDialog(myViewModel, navigationController)
            }
            if (marker != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (marker!!.image != null) {
                        GlideImage(
                            model = "${marker!!.image}",
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .size(250.dp)
                                .background(CoolGray2)
                                .border(
                                    width = 1.dp,
                                    color = CoolGray2,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        )
                    } else {
                        Image(
                            painter = when (marker!!.category) {
                                categories?.get(0)?.name -> painterResource(id = R.drawable.locationlogopink)
                                categories?.get(1)?.name -> painterResource(id = R.drawable.locationlogoblue)
                                categories?.get(2)?.name -> painterResource(id = R.drawable.locationlogogreen)
                                categories?.get(3)?.name -> painterResource(id = R.drawable.locationlogored)
                                else -> painterResource(id = R.drawable.locationlogo)
                            },
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(250.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    when (marker!!.category) {
                        categories?.get(0)?.name -> categories?.get(0)?.color
                        categories?.get(1)?.name -> categories?.get(1)?.color
                        categories?.get(2)?.name -> categories?.get(2)?.color
                        categories?.get(3)?.name -> categories?.get(3)?.color
                        else -> Jasmine
                    }?.let {
                        Text(
                            text = marker!!.title,
                            fontSize = 36.sp,
                            style = TextStyle(
                                fontFamily = lemonMilkMediumItalic,
                                letterSpacing = TextUnit(0.7F, TextUnitType.Sp)
                            ),
                            fontWeight = FontWeight.Bold,
                            color = it
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = marker!!.description,
                        fontSize = 20.sp,
                        style = TextStyle(
                            fontFamily = coolveticaRgIt
                        ),
                        color = CoolGray2
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (marker!!.category != null) {
                        Text(
                            text = marker!!.category!!,
                            fontSize = 16.sp,
                            style = TextStyle(
                                fontFamily = coolveticaRgIt
                            )
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = {
                                for (back in 1..2) {
                                    navigationController.navigateUp()
                                }
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Delete Marker"
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenTopAppBar(
    myViewModel: MapsViewModel,
    state: DrawerState,
    navigationController: NavHostController
) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = {
            Text(
                text = "Detail Screen",
                style = TextStyle(
                    fontFamily = lemonMilkBoldItalic
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                navigationController.navigateUp()
            }) {
                Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = "ArrowBack")
            }
        },
        actions = {
            IconButton(onClick = {
                myViewModel.showDialog(true)
            }) {
                Icon(
                    imageVector = Icons.Filled.DeleteForever,
                    contentDescription = "Delete Marker"
                )
            }
        }
    )
}

@Composable
fun DeleteMarkerDialog(myViewModel: MapsViewModel,     navigationController: NavHostController,
) {
    AlertDialog(
        icon = { Icon(Icons.Filled.Delete, contentDescription = "Delete Marker") },
        title = { Text(text = "Delete Marker") },
        text = { Text(text = "Are you sure you want to delete this marker?") },
        onDismissRequest = {
            myViewModel.showDialog(false)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    myViewModel.DeleteMarker(myViewModel.markerIDSelected.value!!.markerID!!)
                    myViewModel.showDialog(false)
                    navigationController.navigate(Routes.SavedMarkersScreen.routes)
                }
            ) {
                Text(text = "Delete")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    myViewModel.showDialog(false)
                }
            ) {
                Text(text = "Cancel")
            }
        }
    )
}