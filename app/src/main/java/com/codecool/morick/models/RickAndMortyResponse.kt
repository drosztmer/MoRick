package com.codecool.morick.models

data class RickAndMortyResponse(
    val info: Info,
    val results: List<RickAndMortyCharacter>
)