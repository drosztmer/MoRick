package com.codecool.morick.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

class Util {

    companion object {

        fun getEpisodeIdsFromUrls(episodeUrls: List<String>): List<String> {
            val ids = mutableListOf<String>()
            for (episodeUrl in episodeUrls) {
                val id = episodeUrl.substringAfterLast('/')
                ids.add(id)
            }
            return ids
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

    }
}