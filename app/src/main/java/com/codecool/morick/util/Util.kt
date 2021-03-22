package com.codecool.morick.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.codecool.morick.R
import com.codecool.morick.models.RickAndMortyCharacter

class Util {

    companion object {

        fun getIdQueryFromUrls(urls: List<String>): String {
            val ids = mutableListOf<String>()
            for (url in urls) {
                val id = url.substringAfterLast('/')
                ids.add(id)
            }
            return ids.joinToString(separator = ",")
        }

        fun getIdFromUrl(url: String): String {
            return url.substringAfterLast('/')
        }

        fun hideKeyboard(activity: Activity) {
            val inputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            val currentFocusedView = activity.currentFocus
            currentFocusedView?.let {
                inputMethodManager.hideSoftInputFromWindow(
                    currentFocusedView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }

        private fun loadText(text: String): String {
            return if (text == Constants.UNKNOWN_LOWERCASE || text.isEmpty()) {
                Constants.UNKNOWN
            } else {
                text
            }
        }

        fun createStringMessageFromCharacter(context: Context, character: RickAndMortyCharacter): String {
            val imageUrlPrefix = context.getString(R.string.image_url) + ": "
            val namePrefix = context.getString(R.string.name) + ": "
            val statusPrefix = context.getString(R.string.status) + ": "
            val speciesPrefix = context.getString(R.string.species) + ": "
            val typePrefix = context.getString(R.string.type) + ": "
            val genderPrefix = context.getString(R.string.gender) + ": "
            val originPrefix = context.getString(R.string.origin) + ": "
            val locationPrefix = context.getString(R.string.location) + ": "
            return imageUrlPrefix + loadText(character.image) + "\n" +
                    namePrefix + loadText(character.name) + "\n" +
                    statusPrefix + loadText(character.status) + "\n" +
                    speciesPrefix + loadText(character.species) + "\n" +
                    typePrefix + loadText(character.type) + "\n" +
                    genderPrefix + loadText(character.gender) + "\n" +
                    originPrefix + loadText(character.origin.name) + "\n" +
                    locationPrefix + loadText(character.location.name)
        }

    }
}