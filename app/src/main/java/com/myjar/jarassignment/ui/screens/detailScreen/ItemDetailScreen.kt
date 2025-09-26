package com.myjar.jarassignment.ui.screens.detailScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.ui.screens.listscreen.ListErrorScreen
import com.myjar.jarassignment.utils.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    itemId: String?,
    viewModel: ItemDetailViewModel,
    navController: NavController
) {

    if (itemId == null) {
        Text("ItemId Not Found")
    } else {
        LaunchedEffect(Unit) {
            viewModel.itemId = itemId
            viewModel.fetchItemDetails(itemId)
        }
        val state by viewModel.state.collectAsState()
        Scaffold(Modifier.fillMaxSize(), topBar = {
            TopAppBar(title = {
                Text(
                    text = "Item Details for ID: $itemId",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }, navigationIcon = {
                IconButton(onClick = navController::popBackStack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            })
        }) { paddingValues ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                when (val item = state) {
                    is Response.Error -> {
                        ListErrorScreen(
                            errorTitle = (item as Response.Error).message,
                            onRetryClick = {
                                viewModel.retry()
                            })
                    }

                    Response.Loading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    is Response.Success<ComputerItem> -> {
                        val data = item.data
                        Text(data.name)
                        data.data?.let { itemData ->
                            itemData.description?.let {
                                Text("Description: ${it}")
                            }
                            itemData.price?.let {
                                Text("Price: $it")
                            }
                        }
                    }
                }
            }
        }
    }
}
