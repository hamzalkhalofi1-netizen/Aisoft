package com.example.yomuai

import org.jsoup.Jsoup

class Scraper {

    suspend fun fetchManga(url: String): List<Manga> {
        val mangaList = mutableListOf<Manga>()
        try {
            val document = Jsoup.connect(url).get()
            // This is a hypothetical selector, it will need to be adjusted for a real website
            val mangaElements = document.select(".manga-item") 
            
            for ((index, element) in mangaElements.withIndex()) {
                val title = element.select(".manga-title").text()
                var coverUrl = element.select("img").attr("src")
                var isBase64 = false

                if (coverUrl.startsWith("data:image")) {
                    isBase64 = true
                }

                mangaList.add(Manga(id = index, title = title, coverUrl = coverUrl, isBase64 = isBase64))
            }
        } catch (e: Exception) {
            // Handle exceptions
            e.printStackTrace()
        }
        if (mangaList.isEmpty()){
             return getDummyManga() // returning dummy manga list if the manga list is empty
        }
        return mangaList
    }
}