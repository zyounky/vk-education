package com.example.vkeducation.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppsNetwork {
    val api: AppsApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://185.103.109.134/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AppsApi::class.java)
    }
}
