package com.codecool.morick.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.codecool.morick.data.database.entities.FavoriteCharacterEntity

@Database(entities = [FavoriteCharacterEntity::class], version = 1, exportSchema = false)
@TypeConverters(FavoriteCharactersTypeConverter::class)
abstract class FavoriteCharactersDatabase: RoomDatabase() {

    abstract fun favoriteCharactersDao(): FavoriteCharactersDao

}