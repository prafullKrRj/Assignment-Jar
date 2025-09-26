package com.myjar.jarassignment.ui.screens.listscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.utils.Response

@Composable
fun ItemListScreen(
    viewModel: JarViewModel,
    onNavigateToDetail: (String) -> Unit,
    navController: NavHostController
) {
    val items by viewModel.data.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.fetchData()
    }
    when (items) {
        is Response.Error -> {
            ListErrorScreen(errorTitle = (items as Response.Error).message)
        }

        Response.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is Response.Success<*> -> {
            SuccessScreen(
                items = (items as Response.Success<List<ComputerItem>>).data,
                viewModel,
            ) {
                navController.navigate("item_detail/${it}")
            }
        }
    }
}

@Composable
fun SuccessScreen(
    items: List<ComputerItem>,
    viewModel: JarViewModel,
    onNavigateToDetail: (String) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    Column(Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = viewModel::updateQuery,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            label = {
                Text("Search..")
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items,
                key = {
                    it.id
                },
            ) { item ->
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            onNavigateToDetail(item.id)
                        }, verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(item.name, fontWeight = FontWeight.SemiBold)
                    item.data?.let { it ->
                        it.price?.let {
                            Text("Price; ${it}")
                        }
                    }
                }
            }
        }
    }
}
