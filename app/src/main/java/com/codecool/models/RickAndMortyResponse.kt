package com.codecool.models

data class RickAndMortyResponse(
    val info: Info,
    val results: List<RickAndMortyCharacter>
)