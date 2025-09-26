package com.myjar.jarassignment.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.myjar.jarassignment.ui.screens.detailScreen.ItemDetailScreen
import com.myjar.jarassignment.ui.screens.listscreen.ItemListScreen
import com.myjar.jarassignment.ui.screens.listscreen.JarViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val navigate = remember { mutableStateOf("") }
    val jarViewModel: JarViewModel = viewModel()
    NavHost(modifier = modifier, navController = navController, startDestination = "item_list") {
        composable("item_list") {
            ItemListScreen(
                viewModel = jarViewModel,
                onNavigateToDetail = { selectedItem -> navigate.value = selectedItem },
                navController = navController
            )
        }
        composable("item_detail/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            ItemDetailScreen(itemId = itemId)
        }
    }
}



