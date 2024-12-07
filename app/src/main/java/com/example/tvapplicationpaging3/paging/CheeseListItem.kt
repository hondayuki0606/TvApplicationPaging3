package com.example.tvapplicationpaging3.paging

import com.example.tvapplicationpaging3.Movie
import com.example.tvapplicationpaging3.dao.Cheese

sealed class CheeseListItem(val name: String) {
    data class Item(val cheese: Cheese) : CheeseListItem(cheese.name)
    data class Separator(private val letter: Char) : CheeseListItem(letter.toUpperCase().toString())
}


sealed class MovieListItem(val name: String) {
    data class Item(val cheese: Movie) : MovieListItem(cheese.title)
    data class Separator(private val letter: Char) : MovieListItem(letter.toUpperCase().toString())
}