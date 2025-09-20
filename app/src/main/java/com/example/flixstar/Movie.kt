package com.example.flixsterplus

data class Movie(
    val title: String,
    val overview: String,
    val posterPath: String
) {
    fun getPosterUrl(): String {
        return if (posterPath.isBlank()) "" else "https://image.tmdb.org/t/p/w500$posterPath"
    }
}
