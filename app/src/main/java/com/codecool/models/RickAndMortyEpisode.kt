package com.codecool.models

data class RickAndMortyEpisode(
    val id: Int,
    val name: String,
    val air_date: String,
    val characters: List<RickAndMortyCharacter>,
    val url: String,
    val created: String
)
