package com.example.snapintuit.network

import okhttp3.ResponseBody

sealed class Resource<out T>{
    object Loading : Resource<Nothing>()
    data class Success<out T>(val data: T): Resource<T>()
    data class Failure(
        val isNetworkFailure: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ) : Resource<Nothing>()

}