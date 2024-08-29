package com.example.snapintuit.repositories

import com.example.snapintuit.models.RegisterRequest
import com.example.snapintuit.network.ApiService


class AuthRepository(
    private val apiService: ApiService
) : BaseRepository() {

    suspend fun login(email: String, password: String) = safeApiCall {
        apiService.login(email, password)
    }
    suspend fun register(registerRequest: RegisterRequest) = safeApiCall {
        apiService.register(registerRequest)
    }
}