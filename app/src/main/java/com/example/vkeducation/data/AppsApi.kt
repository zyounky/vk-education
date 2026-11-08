package com.example.vkeducation.data

import retrofit2.http.GET
import retrofit2.http.Path

interface AppsApi {
    @GET("catalog")
    suspend fun getCatalog(): List<AppDto>

    @GET("catalog/{id}")
    suspend fun getAppDetails(@Path("id") id: String): AppDetailsDto
}
