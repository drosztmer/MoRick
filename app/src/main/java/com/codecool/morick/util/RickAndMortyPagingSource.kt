package com.codecool.morick.util

import com.codecool.morick.data.network.RickAndMortyApi
import javax.inject.Inject

class RickAndMortyPagingSource @Inject constructor(
    private val rickAndMortyApi: RickAndMortyApi,
    private val query: String
)  {
}