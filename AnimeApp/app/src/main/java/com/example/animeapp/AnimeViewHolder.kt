package com.example.animeapp

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.example.animeapp.databinding.ItemAnimeBinding

class AnimeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemAnimeBinding.bind(view)

    fun bind(anime: AnimeItemResponse) {
        binding.tvAnimeTitle.text = anime.title
        Picasso.get().load(anime.images.jpg.imageUrl).into(binding.ivAnime)
    }
}