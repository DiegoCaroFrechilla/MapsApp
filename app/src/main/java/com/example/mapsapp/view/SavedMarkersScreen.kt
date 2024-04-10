package com.example.mapsapp.view

import android.Manifest
import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mapsapp.ui.theme.Jasmine
import com.example.mapsapp.viewmodel.MapsViewModel
import com.example.mapsapp.viewmodel.lemonMilkMedium
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedMarkersScreen(navigationController: NavHostController, myViewModel: MapsViewModel) {

}

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun ScaffoldSavedMarkerScreen(
    navigationController: NavHostController,
    myViewModel: MapsViewModel,
    state: DrawerState
) {
    val sheetState = rememberModalBottomSheetState(true)
    val scope = rememberCoroutineScope()
    val showBottomSheet by myViewModel.showBottom.observeAsState(false)
    var locationName by remember { mutableStateOf("") }
    var locationDescription by remember { mutableStateOf("") }
    val context = LocalContext.current
    val isCameraPermissionGranted by myViewModel.cameraPermissionGranted.observeAsState(false)
    val shouldShowPermissionRationale by myViewModel.shouldShowPermissionRationale.observeAsState(
        false
    )
    myViewModel.getMarkers()
    val markers by myViewModel.markers.observeAsState()
    val showPermissionDenied by myViewModel.showPermissionDenied.observeAsState(false)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                myViewModel.setCameraPermissionGranted(true)
            } else {
                myViewModel.setShouldShowPermissionRationale(
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.CAMERA
                    )
                )
                if (!shouldShowPermissionRationale) {
                    Log.i("CameraScreen", "No podemos volver a pedir permisos")
                    myViewModel.setShowPermissionDenied(true)
                }
            }
        }
    )
    Scaffold(
        topBar = { MyTopAppBar(myViewModel, state) }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
        )
        {
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                markers?.let {
                    items(it.toList()) {
                        MarkerList(marker = it)
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MarkerList(marker: MapsViewModel.Marker) {
    Card(
        border = BorderStroke(2.dp, Jasmine),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color.White,
        contentColor = Color.Black,
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                /**/
            }
    ) {
        Column {
            GlideImage(
                model = marker.image,
                contentDescription = "Marker Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        Column {
            Row {
                Text(
                    text = marker.title
                )
            }
            Row {
                Text(
                    text = marker.description
                )
            }
        }
    }
    /*        Row(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            ) {
                GlideImage(
                    model = marker.image,
                    contentDescription = "Marker Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize(
                )
            }
            Row {
                Text(
                    text = marker.title,
                    fontSize = 28.sp,
                    style = TextStyle(
                        fontFamily = lemonMilkMedium
                    ),
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .padding(start = 8.dp, bottom = 8.dp)
                        .fillMaxSize()
                )
                Text(
                    text = marker.description,
                    fontSize = 16.sp,
                    style = TextStyle(
                        fontFamily = lemonMilkMedium
                    ),
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .padding(start = 8.dp, bottom = 8.dp)
                        .fillMaxSize()
                )
            }
        }*/

}