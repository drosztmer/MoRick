package com.codecool.morick.bindingadapters

import android.util.Log
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.codecool.morick.R
import com.codecool.morick.models.RickAndMortyCharacter
import com.codecool.morick.ui.fragments.CharactersFragmentDirections
import java.lang.Exception

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

        @BindingAdapter("onCharacterClickListener")
        @JvmStatic
        fun onCharacterClickListener(itemCharacterLayout: ConstraintLayout, character: RickAndMortyCharacter) {
            itemCharacterLayout.setOnClickListener {
                try {
                    val action = CharactersFragmentDirections.actionCharactersFragmentToDetailsFragment(character)
                    itemCharacterLayout.findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.d("onCharacterClickListener", e.toString())
                }
            }
        }

    }
}