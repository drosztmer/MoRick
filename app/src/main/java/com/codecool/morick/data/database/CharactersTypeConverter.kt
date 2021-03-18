package com.codecool.morick.data.database

import androidx.room.TypeConverter
import com.codecool.morick.models.RickAndMortyCharacter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CharactersTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun characterToString(character: RickAndMortyCharacter): String {
        return gson.toJson(character)
    }

    @TypeConverter
    fun stringToCharacter(data: String): RickAndMortyCharacter {
        val listType = object : TypeToken<RickAndMortyCharacter>() {}.type
        return gson.fromJson(data, listType)
    }

}