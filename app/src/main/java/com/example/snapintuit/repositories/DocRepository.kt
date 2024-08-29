package com.example.snapintuit.repositories

import android.util.Log
import com.example.snapintuit.models.AIResponse
import com.example.snapintuit.models.PdfData
import com.example.snapintuit.models.UploadResponse
import com.example.snapintuit.network.ApiService
import com.example.snapintuit.network.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File


class DocRepository(
    private val apiService: ApiService
) : BaseRepository() {

    suspend fun getDocs(userId: Int?): Resource<List<PdfData>> = safeApiCall {
        apiService.getDocs(userId)
    }

    suspend fun uploadDoc(
        file: MultipartBody.Part,
        name: RequestBody,
        userId: RequestBody
    ): Resource<UploadResponse> {
        return safeApiCall {
            apiService.uploadDoc(file, name, userId)
        }
    }

    suspend fun getAIResponse(pdfId: Int, prompt: String): Resource<AIResponse> {
        return safeApiCall {
            apiService.getAIResponse(pdfId, prompt)
        }
    }


}
