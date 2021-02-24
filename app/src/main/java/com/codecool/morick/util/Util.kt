package com.codecool.morick.util

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

    }
}