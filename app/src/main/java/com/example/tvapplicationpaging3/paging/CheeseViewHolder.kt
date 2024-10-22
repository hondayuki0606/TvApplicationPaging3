package com.example.tvapplicationpaging3.paging

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tvapplicationpaging3.R

class CheeseViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.cheese_item, parent, false)
) {
    var cheese: Cheese? = null
        private set
    private val nameView = itemView.findViewById<TextView>(R.id.name)

    /**
     * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
     * ViewHolder when Item is loaded.
     */
    fun bindTo(item: CheeseListItem?) {
        if (item is CheeseListItem.Separator) {
            nameView.text = "${item.name} Cheeses"
            nameView.setTypeface(null, Typeface.BOLD)
        } else {
            nameView.text = item?.name
            nameView.setTypeface(null, Typeface.NORMAL)
        }
        cheese = (item as? CheeseListItem.Item)?.cheese
        nameView.text = item?.name
    }
}