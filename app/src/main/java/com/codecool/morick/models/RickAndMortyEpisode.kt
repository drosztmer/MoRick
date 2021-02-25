package com.codecool.morick.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RickAndMortyEpisode(
    val id: Int,
    val name: String,
    val air_date: String,
    val characters: List<RickAndMortyCharacter>,
    val url: String,
    val created: String
): Parcelable