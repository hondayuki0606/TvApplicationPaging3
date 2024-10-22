package com.example.tvapplicationpaging3.paging

sealed class CheeseListItem(val name: String) {
    data class Item(val cheese: Cheese) : CheeseListItem(cheese.name)
    data class Separator(private val letter: Char) : CheeseListItem(letter.toUpperCase().toString())
}