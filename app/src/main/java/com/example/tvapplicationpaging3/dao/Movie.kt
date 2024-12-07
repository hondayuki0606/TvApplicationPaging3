package com.example.tvapplicationpaging3.dao

import androidx.room.Entity

@Entity(tableName = "movie")
data class Movie(
    var id: Long = 0,
    var title: String = "",
    var description: String? = null,
    var backgroundImageUrl: String? = null,
    var cardImageUrl: String? = null,
    var videoUrl: String? = null,
    var studio: String? = null,
)
