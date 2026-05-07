package com.example.newapp.model

data class NewsApiResponse(
    val status: String,
    val totalResults: Int?,
    val articles: List<News>?
)