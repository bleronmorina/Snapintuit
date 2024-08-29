package com.example.snapintuit.network

import com.example.snapintuit.models.AIResponse
import com.example.snapintuit.models.AuthResponse
import com.example.snapintuit.models.PdfData
import com.example.snapintuit.models.RegisterRequest
import com.example.snapintuit.models.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.Url


interface ApiService {
    @FormUrlEncoded
    @POST("api/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): AuthResponse

    @Multipart
    @POST("api/upload")
    suspend fun uploadDoc(
        @Part file: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("user_id") userId: RequestBody
    ): UploadResponse

    @GET("api/user/pdfs")
    suspend fun getDocs(
        @Query("user_id") userId: Int?
    ): List<PdfData>

    @POST("api/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): AuthResponse

    @GET("api/ai")
    suspend fun getAIResponse(
        @Query("pdfs_id") pdfId: Int,
        @Query("prompt") prompt: String
    ): AIResponse

}
