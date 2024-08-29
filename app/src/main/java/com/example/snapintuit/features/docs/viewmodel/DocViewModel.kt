package com.example.snapintuit.features.docs.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snapintuit.models.AIResponse
import com.example.snapintuit.models.PdfData
import com.example.snapintuit.models.UploadResponse
import com.example.snapintuit.network.Resource
import com.example.snapintuit.repositories.DocRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject

class DocViewModel @Inject
constructor(private val docRepository: DocRepository) : ViewModel() {

    private val _docs: MutableLiveData<Resource<List<PdfData>>> = MutableLiveData()
    val docs: LiveData<Resource<List<PdfData>>> get() = _docs

    fun getDocs(userId: Int?) {

        viewModelScope.launch {
            _docs.value = docRepository.getDocs(userId)
        }
    }

    private val _uploadResponse: MutableLiveData<Resource<UploadResponse>> = MutableLiveData()

    val uploadResponse: LiveData<Resource<UploadResponse>> get() = _uploadResponse

    var isFileUploaded: Boolean = false
    fun uploadDoc(file: MultipartBody.Part, name: RequestBody, userId: RequestBody) = viewModelScope.launch {
        if (isFileUploaded) return@launch
        _uploadResponse.value = Resource.Loading
        _uploadResponse.value = docRepository.uploadDoc(file, name, userId)
    }

    private val _aiResponse: MutableLiveData<Resource<AIResponse>> = MutableLiveData()
    val aiResponse: LiveData<Resource<AIResponse>> get() = _aiResponse

    fun getAIResponse(pdfId: Int, prompt: String) = viewModelScope.launch {
        _aiResponse.value = docRepository.getAIResponse(pdfId, prompt)
    }


}
