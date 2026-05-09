package com.example.yomuai

data class Manga(
    val id: Int,
    val title: String,
    val coverUrl: String,
    val isBase64: Boolean = false
)