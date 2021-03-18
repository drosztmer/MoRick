package com.codecool.morick.bindingadapters

import android.util.Log
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.codecool.morick.R
import com.codecool.morick.models.RickAndMortyCharacter
import com.codecool.morick.ui.fragments.characters.CharactersFragmentDirections
import com.codecool.morick.ui.fragments.favoritecharacters.FavoriteCharactersFragmentDirections
import com.codecool.morick.ui.fragments.location.LocationFragmentDirections
import com.codecool.morick.util.Constants.Companion.CHARACTERS
import com.codecool.morick.util.Constants.Companion.FAVORITES
import com.codecool.morick.util.Constants.Companion.LOCATION

class ItemCharacterBinding {

    companion object {

        @BindingAdapter("loadItemImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
            imageView.load(imageUrl) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
        }

        @BindingAdapter("onCharacterClickListener", "fromDestination", requireAll = true)
        @JvmStatic
        fun onCharacterClickListener(itemCharacterLayout: ConstraintLayout, character: RickAndMortyCharacter, from: String) {
            itemCharacterLayout.setOnClickListener {
                if (from == CHARACTERS) {
                    try {
                        val action = CharactersFragmentDirections.actionCharactersFragmentToDetailsFragment(character)
                        itemCharacterLayout.findNavController().navigate(action)
                    } catch (e: Exception) {
                        Log.d("onCharacterClickListener", e.toString())
                    }
                } else if (from == LOCATION) {
                    try {
                        val action = LocationFragmentDirections.actionLocationFragmentToDetailsFragment(character)
                        itemCharacterLayout.findNavController().navigate(action)
                    } catch (e: Exception) {
                        Log.d("onCharacterClickListener", e.toString())
                    }
                } else if (from == FAVORITES) {
                    try {
                        val action = FavoriteCharactersFragmentDirections.actionFavoriteCharactersFragmentToDetailsFragment(character)
                        itemCharacterLayout.findNavController().navigate(action)
                    } catch (e: Exception) {
                        Log.d("onCharacterClickListener", e.toString())
                    }
                }

            }
        }

    }
}