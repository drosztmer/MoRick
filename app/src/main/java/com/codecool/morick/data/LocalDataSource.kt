package com.codecool.morick.data

import com.codecool.morick.data.database.FavoriteCharactersDao
import com.codecool.morick.data.database.entities.FavoriteCharacterEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val favoriteCharactersDao: FavoriteCharactersDao
) {

    suspend fun insertFavoriteCharacter(favoriteCharacterEntity: FavoriteCharacterEntity) {
        favoriteCharactersDao.insertFavoriteCharacter(favoriteCharacterEntity)
    }

    suspend fun deleteFavoriteCharacter(favoriteCharacterEntity: FavoriteCharacterEntity) {
        favoriteCharactersDao.deleteFavoriteCharacter(favoriteCharacterEntity)
    }

    fun readFavoriteCharacters(): Flow<List<FavoriteCharacterEntity>> {
        return favoriteCharactersDao.readFavoriteCharacters()
    }

    suspend fun deleteAllFavoriteCharacters() {
        favoriteCharactersDao.deleteAllFavoriteCharacters()
    }


}