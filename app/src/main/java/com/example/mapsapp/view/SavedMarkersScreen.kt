package com.example.mapsapp.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mapsapp.R
import com.example.mapsapp.Routes
import com.example.mapsapp.model.Marker
import com.example.mapsapp.model.MarkersCategories
import com.example.mapsapp.ui.theme.CoolGray2
import com.example.mapsapp.ui.theme.Jasmine
import com.example.mapsapp.ui.theme.PrussianBlue
import com.example.mapsapp.viewmodel.MapsViewModel
import com.example.mapsapp.viewmodel.lemonMilkMediumItalic
import com.example.mapsapp.viewmodel.lemonMilkRegularItalic

@Composable
fun SavedMarkersScreen(navigationController: NavHostController, myViewModel: MapsViewModel) {

}

@Composable
fun ScaffoldSavedMarkerScreen(
    navigationController: NavHostController,
    myViewModel: MapsViewModel,
    state: DrawerState
) {
    myViewModel.getMarkers()
    val markers by myViewModel.markers.observeAsState()
    val categories by myViewModel.categories.observeAsState()
    Scaffold(
        topBar = { MyTopAppBar(myViewModel, state, navigationController) }
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
                        MarkerList(marker = it, categories, navigationController, myViewModel)
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MarkerList(
    marker: Marker,
    categories: List<MarkersCategories>?,
    navigationController: NavHostController,
    myViewModel: MapsViewModel
) {
    Card(
        border = BorderStroke(2.dp, Jasmine),
        shape = AbsoluteCutCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = PrussianBlue,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                myViewModel.selectMarker(marker)
                navigationController.navigate(Routes.DetailScreen.routes)
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (marker.image != null) {
                GlideImage(
                    model = marker.image,
                    contentDescription = "Marker Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(150.dp)
                        .aspectRatio(1F)
                )
            } else {
                Image(
                    painter = when (marker.category) {
                        categories?.get(0)?.name -> painterResource(id = R.drawable.locationlogopink)
                        categories?.get(1)?.name -> painterResource(id = R.drawable.locationlogoblue)
                        categories?.get(2)?.name -> painterResource(id = R.drawable.locationlogogreen)
                        categories?.get(3)?.name -> painterResource(id = R.drawable.locationlogored)
                        else -> painterResource(id = R.drawable.locationlogo)
                    },
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(150.dp)
                        .aspectRatio(1F)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                when (marker!!.category) {
                    categories?.get(0)?.name -> categories?.get(0)?.color
                    categories?.get(1)?.name -> categories?.get(1)?.color
                    categories?.get(2)?.name -> categories?.get(2)?.color
                    categories?.get(3)?.name -> categories?.get(3)?.color
                    else -> Jasmine
                }?.let {
                    Text(
                        text = marker.title,
                        style = TextStyle(
                            fontFamily = lemonMilkRegularItalic,
                            color = it
                        )
                    )
                }
                Text(
                    text = marker.description,
                    style = TextStyle(
                        fontFamily = lemonMilkRegularItalic,
                        color = CoolGray2
                    )
                )
            }

        }
    }
}


@Composable
fun Filter(
    navigationController: NavHostController, myViewModel: MapsViewModel,
) {
    val navBackStackEntry by navigationController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val category by myViewModel.categoryFilter.observeAsState()
    //if (currentRoute == Routes.SavedMarkersScreen.routes) {
    IconButton(
        onClick = {
            isDropdownExpanded = !isDropdownExpanded
        },
        content = {
            Icon(
                imageVector = Icons.Filled.FilterList,
                contentDescription = "Filter"
            )
        }
    )
    DropdownMenu(
        expanded = isDropdownExpanded,
        onDismissRequest = { isDropdownExpanded = false },
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.background
            )
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = "All",
                    color = CoolGray2,
                    style = TextStyle(fontFamily = lemonMilkMediumItalic)
                )
            },
            onClick = {
                myViewModel.getMarkers()
            }
        )
        myViewModel.categories.value?.forEach { item ->
            DropdownMenuItem(
                enabled = category != item.name,
                text = {
                    Text(
                        text = item.name,
                        color = item.color,
                        style = TextStyle(fontFamily = lemonMilkMediumItalic)
                    )
                },
                onClick = {
                    myViewModel.changeCategoryFilter(item.name)
                    myViewModel.getMarkersFiltered(item.name)
                }
            )
        }
    }
}
//}


