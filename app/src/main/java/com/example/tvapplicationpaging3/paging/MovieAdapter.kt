package com.example.tvapplicationpaging3.paging

import android.graphics.Color
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tvapplicationpaging3.Movie

/**
 * A simple PagedListAdapter that binds Cheese items into CardViews.
 *
 * PagedListAdapter is a RecyclerView.Adapter base class which can present the content of PagedLists
 * in a RecyclerView. It requests new pages as the user scrolls, and handles new PagedLists by
 * computing list differences on a background thread, and dispatching minimal, efficient updates to
 * the RecyclerView to ensure minimal UI thread work.
 *
 * If you want to use your own Adapter base class, try using a PagedListAdapterHelper inside your
 * adapter instead.
 *
 * @see androidx.paging.PagedListAdapter
 * @see androidx.paging.AsyncPagedListDiffer
 */
class MovieAdapter : PagingDataAdapter<Movie, MovieViewHolder>(diffCallback) {
    // 選択されたアイテムのポジションを保持する変数
    private var selectedPosition: Int =  RecyclerView.NO_POSITION
    private var longPressPosition: Int = RecyclerView.NO_POSITION
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
//        holder.bindTo(getItem(position))
        val item = getItem(position)
        val selectedPosition = position
        if (item != null) {
            holder.bindTo(item)
            // 選択されたアイテムの色を更新
            when (selectedPosition) {
                selectedPosition -> holder.itemView.setBackgroundColor(Color.GRAY) // 通常選択
                longPressPosition -> holder.itemView.setBackgroundColor(Color.RED) // 長押し選択
                else -> holder.itemView.setBackgroundColor(Color.WHITE) // 非選択
            }
            // 長押しリスナーの設定
            holder.itemView.setOnLongClickListener {
                val previousLongPressPosition = longPressPosition
                longPressPosition = selectedPosition
                notifyItemChanged(previousLongPressPosition)
                notifyItemChanged(longPressPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(parent)
    }
    fun updateSelectedPosition(newPosition: Int) {
        if (newPosition == selectedPosition) return // 位置が変わらなければ何もしない

        val previousPosition = selectedPosition
        selectedPosition = newPosition
        notifyItemChanged(previousPosition)
        notifyItemChanged(selectedPosition)
    }
    companion object {
        /**
         * This diff callback informs the PagedListAdapter how to compute list differences when new
         * PagedLists arrive.
         *
         * When you add a Cheese with the 'Add' button, the PagedListAdapter uses diffCallback to
         * detect there's only a single item difference from before, so it only needs to animate and
         * rebind a single view.
         *
         * @see DiffUtil
         */
        val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            /**
             * Note that in kotlin, == checking on data classes compares all contents, but in Java,
             * typically you'll implement Object#equals, and use it to compare object contents.
             */
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }
}