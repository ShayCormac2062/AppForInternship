package com.example.internshipapp.retrofit

import com.example.internshipapp.entity.Area
import retrofit2.Call
import retrofit2.http.GET

interface APIService {

    @GET("/areas")
    fun getAllAreas(): Call<List<Area>>
}