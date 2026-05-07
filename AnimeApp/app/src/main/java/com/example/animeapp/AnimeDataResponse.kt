package com.example.animeapp

import com.google.gson.annotations.SerializedName

data class AnimeDataResponse(
    @SerializedName("data") val data: List<AnimeItemResponse>
)

data class AnimeItemResponse(
    @SerializedName("mal_id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("images") val images: AnimeImageResponse
)

data class AnimeImageResponse(
    @SerializedName("jpg") val jpg: AnimeJpgResponse
)

data class AnimeJpgResponse(
    @SerializedName("image_url") val imageUrl: String
)