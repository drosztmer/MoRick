package com.codecool.morick.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

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

    }
}