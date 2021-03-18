package com.codecool.morick.bindingadapters

import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codecool.morick.adapters.CharactersAdapter
import com.codecool.morick.data.database.entities.FavoriteCharacterEntity
import com.codecool.morick.models.RickAndMortyCharacter

class FavoriteCharactersBinding {

    companion object {

        @BindingAdapter("setVisibility", "setData", requireAll = false)
        @JvmStatic
        fun setVisibility(
            view: View,
            favoriteCharacterEntityList: List<FavoriteCharacterEntity>?,
            adapter: CharactersAdapter?
        ) {
            when (view) {
                is RecyclerView -> {
                    val dataCheck = favoriteCharacterEntityList.isNullOrEmpty()
                    view.isInvisible = dataCheck
                    if (!dataCheck) {
                        val rickAndMortyCharacters: List<RickAndMortyCharacter>? = favoriteCharacterEntityList?.map { it.character }
                        rickAndMortyCharacters?.let { adapter?.setData(it) }
                    }
                } else -> view.isVisible = favoriteCharacterEntityList.isNullOrEmpty()
            }
        }

    }
}