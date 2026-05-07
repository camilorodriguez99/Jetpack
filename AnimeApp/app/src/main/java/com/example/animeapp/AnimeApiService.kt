package com.example.animeapp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AnimeApiService {
    @GET("anime")
    suspend fun getAnimes(@Query("q") animeName: String): Response<AnimeDataResponse>
}