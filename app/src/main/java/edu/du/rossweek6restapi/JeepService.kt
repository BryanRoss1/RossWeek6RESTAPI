package edu.du.rossweek6restapi

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface JeepService {
    @POST("jeeps")
    suspend fun createJeep(@Body requestBody: RequestBody): Response<ResponseBody>

    @GET("jeeps")
    suspend fun getJeeps(): Response<ResponseBody>

    @GET("jeeps/{id}")
    suspend fun getJeep(@Path("id") id: String): Response<ResponseBody>

    @PUT("jeeps/{id}")
    suspend fun updateJeep(@Path("id") id: String, @Body requestBody: RequestBody): Response<ResponseBody>

    @DELETE("jeeps/{id}")
    suspend fun deleteJeep(@Path("id") id: String): Response<ResponseBody>
}