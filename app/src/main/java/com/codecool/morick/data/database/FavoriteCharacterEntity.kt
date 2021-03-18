package com.codecool.morick.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codecool.morick.models.RickAndMortyCharacter
import com.codecool.morick.util.Constants.Companion.FAVORITE_CHARACTERS_TABLE

@Entity(tableName = FAVORITE_CHARACTERS_TABLE)
class FavoriteCharacterEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var character: RickAndMortyCharacter
)