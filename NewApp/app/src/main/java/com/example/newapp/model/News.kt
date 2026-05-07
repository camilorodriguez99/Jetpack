package com.example.newapp.model

data class News(
    val title: String,
    val content: String?,
    val author: String?,
    val url: String,
    val urlToImage: String?
)