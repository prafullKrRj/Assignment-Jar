package com.myjar.jarassignment.ui.screens.listscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myjar.jarassignment.createRetrofit
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.data.repository.JarRepository
import com.myjar.jarassignment.data.repository.JarRepositoryImpl
import com.myjar.jarassignment.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JarViewModel : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    val searchQuery = _searchQuery.asStateFlow()
    private val _computerItemData = MutableStateFlow<Response<List<ComputerItem>>>(Response.Loading)
    val computerItemData = _computerItemData.asStateFlow()
    private val repository: JarRepository = JarRepositoryImpl(createRetrofit())
    val data = combine(
        _searchQuery,
        _computerItemData
    ) { query, response ->
        withContext(Dispatchers.Default) {
            when (response) {
                is Response.Error -> response
                Response.Loading -> response
                is Response.Success<List<ComputerItem>> -> {
                    if (query.isEmpty()) {
                        response
                    } else {
                        val data = response.data.asSequence().filter { item ->
                            item.name.contains(query, ignoreCase = true)
                        }.toList()
                        Response.Success(data)
                    }
                }
            }

        }
    }.stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), Response.Loading)




    fun fetchData() {
        _computerItemData.update {
            Response.Loading
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchResults().collectLatest { response ->
                _computerItemData.update {
                    response
                }
                Log.d("Main Screen", "${computerItemData.value}")
            }
        }
    }

    fun updateQuery(query: String) {
        _searchQuery.update {
            query
        }
    }

    fun retry() = fetchData()
}