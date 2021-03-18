package com.codecool.morick.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCharactersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteCharacter(favoriteCharacterEntity: FavoriteCharacterEntity)

    @Delete
    suspend fun deleteFavoriteCharacter(favoriteCharacterEntity: FavoriteCharacterEntity)

    @Query("SELECT * FROM favorite_characters_table ORDER BY id ASC")
    fun readFavoriteCharacters(): Flow<List<FavoriteCharacterEntity>>

    @Query("DELETE FROM favorite_characters_table")
    suspend fun deleteAllFavoriteCharacters()

}