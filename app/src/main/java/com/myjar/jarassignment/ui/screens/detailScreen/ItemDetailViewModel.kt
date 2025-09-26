package com.myjar.jarassignment.ui.screens.detailScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myjar.jarassignment.createRetrofit
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.data.repository.JarRepository
import com.myjar.jarassignment.data.repository.JarRepositoryImpl
import com.myjar.jarassignment.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ItemDetailViewModel : ViewModel() {


    var itemId by mutableStateOf("")
    private val repository: JarRepository = JarRepositoryImpl(createRetrofit())
    private val _item = MutableStateFlow<Response<ComputerItem>>(Response.Loading)
    val state = _item.asStateFlow()
    fun fetchItemDetails(id: String) {

        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchItemDetails(itemId).collectLatest { response ->
                _item.update { response }
            }
        }
    }

    fun retry() = fetchItemDetails(itemId)
}