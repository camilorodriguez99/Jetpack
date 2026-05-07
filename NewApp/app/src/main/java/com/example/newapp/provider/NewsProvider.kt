package com.example.newapp.provider

import com.example.newapp.model.NewsApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "fe970170c1fe41cb81bd82c77c185ae0"

interface NewsProvider {
    @GET("top-headlines?apiKey=$API_KEY")
    suspend fun topHeadlines(@Query("country") country: String): Response<NewsApiResponse>
}