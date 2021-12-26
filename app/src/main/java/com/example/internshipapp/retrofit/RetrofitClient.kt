package com.example.internshipapp.retrofit

import com.example.internshipapp.entity.Area
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val PARSE_ROOT = "https://api.hh.ru"

    fun getRetrofitInstance(): Retrofit = Retrofit.Builder()
        .baseUrl(PARSE_ROOT)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getAPIService(retrofit: Retrofit): APIService = retrofit.create(APIService::class.java)
}