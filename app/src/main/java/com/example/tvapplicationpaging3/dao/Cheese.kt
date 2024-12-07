package com.example.tvapplicationpaging3.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class that represents our items.
 */
@Entity
data class Cheese(@PrimaryKey(autoGenerate = true) val id: Int, var name: String)