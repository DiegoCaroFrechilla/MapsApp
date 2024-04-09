package com.example.mapsapp.view

import android.Manifest
import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.example.mapsapp.Routes
import com.example.mapsapp.ui.theme.CoolGray2
import com.example.mapsapp.ui.theme.Gunmetal
import com.example.mapsapp.ui.theme.Jasmine
import com.example.mapsapp.viewmodel.MapsViewModel
import com.example.mapsapp.viewmodel.lemonMilkMediumItalic
import com.example.mapsapp.viewmodel.lemonMilkRegularItalic
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

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
        Box(modifier = Modifier.padding(contentPadding))
        {
            Text(text = "Hola")
        }
    }
}
