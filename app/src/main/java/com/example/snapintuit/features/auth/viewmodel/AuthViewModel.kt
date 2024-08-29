package com.example.snapintuit.features.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snapintuit.models.AuthResponse
import com.example.snapintuit.models.RegisterRequest
import com.example.snapintuit.network.Resource
import com.example.snapintuit.repositories.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _authResponse : MutableLiveData<Resource<AuthResponse>> = MutableLiveData()

    val authResponse : LiveData<Resource<AuthResponse>> get() = _authResponse

    fun login (email: String, password: String) = viewModelScope.launch {
        _authResponse.value = authRepository.login(email, password)
    }


    fun register(name: String, email: String, password: String) = viewModelScope.launch {
        val registerRequest = RegisterRequest(name, email, password)
        _authResponse.value = authRepository.register(registerRequest)
    }




















//    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
//    val loginResult: LiveData<Result<LoginResponse>> get() = _loginResult
//
//    fun login(loginRequest: LoginRequest) {
//        authRepository.login(loginRequest).enqueue(object : Callback<LoginResponse> {
//            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                _loginResult.postValue(Result.failure(t))
//            }
//
//            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//                if (response.isSuccessful) {
//                    _loginResult.postValue(Result.success(response.body()))
//                } else {
//                    _loginResult.postValue(Result.failure(Exception("Login failed")))
//                }
//            }
//        })
//    }
}
