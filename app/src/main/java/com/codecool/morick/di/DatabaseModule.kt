package com.codecool.morick.di

import android.content.Context
import androidx.room.Room
import com.codecool.morick.data.database.FavoriteCharactersDatabase
import com.codecool.morick.util.Constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, FavoriteCharactersDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideDao(database: FavoriteCharactersDatabase) = database.favoriteCharactersDao()

}