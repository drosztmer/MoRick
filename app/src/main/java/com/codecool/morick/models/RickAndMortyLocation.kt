package com.codecool.morick.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RickAndMortyLocation(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>
): Parcelable
