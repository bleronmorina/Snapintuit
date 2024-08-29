package com.example.snapintuit.features.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.snapintuit.features.auth.viewmodel.AuthViewModel
import com.example.snapintuit.features.docs.viewmodel.DocViewModel
import com.example.snapintuit.repositories.AuthRepository
import com.example.snapintuit.repositories.BaseRepository
import com.example.snapintuit.repositories.DocRepository

class ViewModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository as AuthRepository) as T
            modelClass.isAssignableFrom(DocViewModel::class.java) -> DocViewModel(repository as DocRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
