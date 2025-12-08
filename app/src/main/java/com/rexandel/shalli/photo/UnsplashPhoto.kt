package com.rexandel.shalli.photo

data class UnsplashPhoto(
    val id: String,
    val urls: UnsplashUrls
)

data class UnsplashUrls(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String
)

data class UnsplashResponse(
    val results: List<UnsplashPhoto>
)