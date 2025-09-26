package com.myjar.jarassignment.data.repository

import com.myjar.jarassignment.data.api.ApiService
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

interface JarRepository {
    suspend fun fetchResults(): Flow<Response<List<ComputerItem>>>
}

class JarRepositoryImpl(
    private val apiService: ApiService
) : JarRepository {
    override suspend fun fetchResults(): Flow<Response<List<ComputerItem>>> = flow {
        try {
            val response = apiService.fetchResults()
            emit(Response.Success(response))
        } catch (e: HttpException) {
            emit(Response.Error("HTTP ERROR"))
        } catch (e: IOException) {
            emit(Response.Error("IO EXCEPTION"))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: "Error"))
        }
    }
}
