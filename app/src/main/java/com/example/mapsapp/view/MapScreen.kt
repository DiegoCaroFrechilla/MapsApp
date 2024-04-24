package com.example.mapsapp.view

import ScaffoldLoginScreen
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.mapsapp.MainActivity
import com.example.mapsapp.Routes
import com.example.mapsapp.model.DrawerItems
import com.example.mapsapp.model.MarkersCategories
import com.example.mapsapp.model.UserPrefs
import com.example.mapsapp.ui.theme.CoolGray2
import com.example.mapsapp.ui.theme.Gunmetal
import com.example.mapsapp.ui.theme.Jasmine
import com.example.mapsapp.viewmodel.MapsViewModel
import com.example.mapsapp.viewmodel.lemonMilkBoldItalic
import com.example.mapsapp.viewmodel.lemonMilkMediumItalic
import com.example.mapsapp.viewmodel.lemonMilkRegularItalic
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.example.mapsapp.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MapScreen(navigationController: NavHostController, myViewModel: MapsViewModel) {
    Mydrawer(myViewModel, navigationController)
}

@Composable
fun Mydrawer(myViewModel: MapsViewModel, navigationController: NavHostController) {
    val scope = rememberCoroutineScope()
    val state: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerItems = listOf(
        DrawerItems.Map,
        DrawerItems.SavedMarkers
    )

    val navBackStackEntry by navigationController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current
    val userPrefs = UserPrefs(context)
    val storedUserData = userPrefs.getUserData.collectAsState(initial = emptyList())
    ModalNavigationDrawer(
        drawerState = state,
        gesturesEnabled = false,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(0.7F),
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerContentColor = MaterialTheme.colorScheme.primary
            ) {
                Row {
                    IconButton(
                        onClick = {
                            scope.launch {
                                state.close()
                            }
                        },
                        Modifier.padding(top = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close Button",
                        )
                    }
                    Text(
                        "Menu",
                        style = TextStyle(
                            fontFamily = lemonMilkBoldItalic
                        ),
                        modifier = Modifier
                            .padding(top = 30.dp)
                    )
                }
                Divider()
                drawerItems.forEach { item ->
                    androidx.compose.material3.NavigationDrawerItem(label = {
                        Text(
                            text = item.label,
                            style = TextStyle(
                                fontFamily = lemonMilkRegularItalic,
                                color = if (currentRoute == item.route) {
                                    Jasmine
                                } else {
                                    CoolGray2
                                }
                            )
                        )
                    },
                        selected = false,
                        onClick = {
                            if (currentRoute != item.route) {
                                navigationController.navigate(item.route)
                                scope.launch {
                                    state.close()
                                }
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.weight(1F))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Logout,
                        contentDescription = "LogOut",
                        modifier = Modifier.padding(
                            bottom = 30.dp,
                            end = 10.dp,
                        ),
                    )
                    Text(
                        text = "Log out",
                        Modifier
                            .clickable {
                                myViewModel.logout()
                                CoroutineScope(Dispatchers.IO).launch {
                                    userPrefs.saveUserData(
                                        storedUserData.value[0],
                                        storedUserData.value[1],
                                        "false"
                                    )
                                }
                                navigationController.navigate(Routes.LoginScreen.routes)
                            }
                            .padding(bottom = 20.dp),
                        style = TextStyle(
                            fontFamily = lemonMilkBoldItalic
                        )
                    )
                }
            }
        }
    ) {
        when (currentRoute) {
            Routes.LoginScreen.routes -> ScaffoldLoginScreen(navigationController, myViewModel)
            Routes.RegisterScreen.routes -> ScaffoldRegisterScreen(
                navigationController,
                myViewModel
            )

            Routes.MapScreen.routes -> ScaffoldMapScreen(navigationController, myViewModel, state)
            Routes.SavedMarkersScreen.routes -> ScaffoldSavedMarkerScreen(
                navigationController,
                myViewModel,
                state
            )

            Routes.DetailScreen.routes -> ScaffoldDetailScreen(
                navigationController,
                myViewModel,
                state
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    myViewModel: MapsViewModel,
    state: DrawerState,
    navigationController: NavHostController
) {

    val scope = rememberCoroutineScope()
    TopAppBar(
        title = {
            Text(
                text = "My SuperApp",
                style = TextStyle(
                    fontFamily = lemonMilkBoldItalic
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    state.open()
                }
            }) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            Filter(navigationController, myViewModel)

        }

    )
}


@OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class, ExperimentalGlideComposeApi::class
)
@Composable
fun ScaffoldMapScreen(
    navigationController: NavHostController,
    myViewModel: MapsViewModel,
    state: DrawerState
) {
    val sheetState = rememberModalBottomSheetState(true)
    val scope = rememberCoroutineScope()
    val showBottomSheet by myViewModel.showBottom.observeAsState(false)
    val locationName by myViewModel.locationName.observeAsState()
    val locationDescription by myViewModel.locationDescription.observeAsState()
    val category by myViewModel.category.observeAsState()
    val context = LocalContext.current
    val isCameraPermissionGranted by myViewModel.cameraPermissionGranted.observeAsState(false)
    val shouldShowPermissionRationale by myViewModel.shouldShowPermissionRationale.observeAsState(
        false
    )
    val uri by myViewModel.imageUri.observeAsState()
    val image by myViewModel.image.observeAsState()
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
        topBar = { MyTopAppBar(myViewModel, state, navigationController) }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding))
        {
            val categories by myViewModel.categories.observeAsState()
            val permissionState =
                rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
            LaunchedEffect(Unit) {
                permissionState.launchPermissionRequest()
            }
            if (permissionState.status.isGranted) {
                Map(myViewModel, categories!!)
                myViewModel.getMarkers()
                if (showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            myViewModel.hideBottomSheet()
                        },
                        sheetState = sheetState
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                        ) {
                            TextBottomSheet(
                                text = "Name",
                                lemonMilkMediumItalic,
                                topPadding = 0,
                                bottomPadding = 5,
                            )
                            OutlinedTextField(
                                value = locationName!!,
                                onValueChange = { myViewModel.changeTitle(it) },
                                textStyle = TextStyle(fontFamily = lemonMilkRegularItalic),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    textColor = Jasmine,
                                    focusedBorderColor = CoolGray2,
                                    unfocusedBorderColor = CoolGray2,
                                    cursorColor = Jasmine
                                )
                            )

                            TextBottomSheet(
                                text = "Description",
                                fontFamily = lemonMilkMediumItalic,
                                topPadding = 10,
                                bottomPadding = 5
                            )
                            OutlinedTextField(
                                value = locationDescription!!,
                                onValueChange = { myViewModel.changeDescription(it) },
                                textStyle = TextStyle(fontFamily = lemonMilkRegularItalic),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    textColor = Jasmine,
                                    focusedBorderColor = CoolGray2,
                                    unfocusedBorderColor = CoolGray2,
                                    cursorColor = Jasmine
                                )
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                if (image != null) {
                                    Image(
                                        bitmap = image!!.asImageBitmap(),
                                        contentDescription = "Image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .size(250.dp)
                                            .border(
                                                width = 1.dp,
                                                color = CoolGray2,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                            ) {
                                categories!!.forEach { item ->
                                    Button(
                                        onClick = {
                                            myViewModel.changeCategory(/*if (item.name == "Default") "" else*/
                                                item.name
                                            )
                                        },
                                        enabled = category != item.name,
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(2.dp, CoolGray2),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Gunmetal,
                                            contentColor = item.color,
                                        ),
                                        modifier = Modifier
                                            .padding(end = 10.dp, top = 10.dp)
                                    ) {
                                        Text(
                                            text = item.name,
                                            style = TextStyle(fontFamily = lemonMilkMediumItalic)
                                        )

                                    }
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp, bottom = 20.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = {
                                        if (!isCameraPermissionGranted) {
                                            launcher.launch(android.Manifest.permission.CAMERA)
                                        } else {
                                            navigationController.navigate(Routes.TakePhotoScreen.routes)
                                        }
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = Gunmetal
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PhotoCamera,
                                        contentDescription = "Take photo",
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Button(
                                    onClick = {
                                        if (uri != null) {
                                            myViewModel.uploadImage(uri)
                                        } else {
                                            myViewModel.CreateMarker(
                                                locationName!!,
                                                locationDescription!!,
                                                null,
                                                category
                                            )
                                        }
                                        myViewModel.getMarkers()
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = Gunmetal
                                    )
                                ) {
                                    Text(
                                        text = "Add Marker",
                                        style = TextStyle(fontFamily = lemonMilkMediumItalic)
                                    )
                                }
                            }
                        }
                        if (showPermissionDenied) {
                            PermissionDeclinedScreen()
                        }
                    }
                }
            } else {
                Text(text = "Need permission")
            }
        }
    }
}

@Composable
fun TextBottomSheet(text: String, fontFamily: FontFamily, topPadding: Int, bottomPadding: Int) {
    Text(
        text = text,
        modifier = Modifier
            .padding(top = topPadding.dp, bottom = bottomPadding.dp),
        style = TextStyle(
            fontFamily = fontFamily,
        )
    )
}

@SuppressLint("MissingPermission")
@Composable
fun Map(myViewModel: MapsViewModel, categories: List<MarkersCategories>) {
    val context = LocalContext.current
    val fusedLocationProviderClient =
        remember { LocationServices.getFusedLocationProviderClient(context) }
    var lastKnowLocation by remember { mutableStateOf<Location?>(null) }
    var deviceLatLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    val cameraPositionState =
        rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(deviceLatLng, 18f) }
    val locationResult = fusedLocationProviderClient.getCurrentLocation(100, null)

    locationResult.addOnCompleteListener(context as MainActivity) { task ->
        if (task.isSuccessful) {
            lastKnowLocation = task.result
            deviceLatLng = LatLng(lastKnowLocation!!.latitude, lastKnowLocation!!.longitude)
            cameraPositionState.position = CameraPosition.fromLatLngZoom(deviceLatLng, 18f)
        } else {
            Log.e("Error", "Exception: %s", task.exception)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val itb = LatLng(41.453465, 2.1837151)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(itb, 10f)
        }
        GoogleMap(
            modifier = Modifier
                .fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = {
            }, onMapLongClick = {
                myViewModel.showBottomSheet(it)
            }
        ) {
            Marker(
                state = MarkerState(position = itb),
                title = "ITB",
                snippet = "Marker at ITB",
                icon = bitmapDescriptorFromVector(context, R.drawable.locationlogo)
            )
            val markers by myViewModel.markers.observeAsState()
            markers?.forEach {

                Marker(
                    state = MarkerState(LatLng(it.latitude, it.longitude)),
                    title = it.title,
                    snippet = it.description,
                    icon = when (it.category) {
                        categories[0].name -> bitmapDescriptorFromVector(
                            context,
                            R.drawable.locationlogopink
                        )

                        categories[1].name -> bitmapDescriptorFromVector(
                            context,
                            R.drawable.locationlogoblue
                        )

                        categories[2].name -> bitmapDescriptorFromVector(
                            context,
                            R.drawable.locationlogogreen
                        )

                        categories[3].name -> bitmapDescriptorFromVector(
                            context,
                            R.drawable.locationlogored
                        )

                        else -> bitmapDescriptorFromVector(context, R.drawable.locationlogo)
                    },
                )
            }
        }
    }
}

//https://stackoverflow.com/questions/42365658/custom-marker-in-google-maps-in-android-with-vector-asset-icon
private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
    val measurament = 150
    return ContextCompat.getDrawable(context, vectorResId)?.run {
        setBounds(0, 0, measurament, measurament)
        val bitmap = Bitmap.createBitmap(measurament, measurament, Bitmap.Config.ARGB_8888)
        draw(Canvas(bitmap))
        BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}

