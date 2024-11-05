package com.example.tvapplicationpaging3.paging

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tvapplicationpaging3.Movie
import com.example.tvapplicationpaging3.R

class MovieViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.movie_model_item, parent, false)
) {
    private val nameView = itemView.findViewById<TextView>(R.id.name)
    private val imageView = itemView.findViewById<ImageView>(R.id.image)

    /**
     * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
     * ViewHolder when Item is loaded.
     */
    fun bindTo(item: Movie?) {
        nameView.setTypeface(null, Typeface.BOLD)
        nameView.text = item?.title
        imageView.setImageResource(R.drawable.movie)
        itemView.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                imageView.setImageResource(R.drawable.app_icon_your_company)
            } else {
                imageView.setImageResource(R.drawable.movie)
            }
        }
        imageView.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                imageView.setImageResource(R.drawable.app_icon_your_company)
            } else {
                imageView.setImageResource(R.drawable.movie)
            }
        }
    }
}