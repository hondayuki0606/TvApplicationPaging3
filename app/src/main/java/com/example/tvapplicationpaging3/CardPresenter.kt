package com.example.tvapplicationpaging3

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.example.tvapplicationpaging3.paging.CheeseListItem
import com.example.tvapplicationpaging3.paging.MoviePagingSource
import com.example.tvapplicationpaging3.paging.MoviePagingSource.Companion.IMAGE_URL
import kotlin.properties.Delegates

/**
 * A CardPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an ImageCardView.
 */
class CardPresenter : Presenter() {
    private var mDefaultCardImage: Drawable? = null
    private var sSelectedBackgroundColor: Int by Delegates.notNull()
    private var sDefaultBackgroundColor: Int by Delegates.notNull()

    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        Log.d("TAG", "honda onBindViewHolder parent $parent")
        sDefaultBackgroundColor = ContextCompat.getColor(parent.context, R.color.default_background)
        sSelectedBackgroundColor =
            ContextCompat.getColor(parent.context, R.color.selected_background)
        mDefaultCardImage = ContextCompat.getDrawable(parent.context, R.drawable.movie)

        val cardView = object : ImageCardView(parent.context) {
            override fun setSelected(selected: Boolean) {
                updateCardBackgroundColor(this, selected)
                super.setSelected(selected)
            }
        }

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        updateCardBackgroundColor(cardView, false)
        return Presenter.ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder?, item: Any?) {
        Log.d("TAG", "honda onBindViewHolder viewHolder $viewHolder")
        val cardView = viewHolder?.view as ImageCardView
        when (item) {
            is Movie -> {
                setMoviePresenter(item, cardView, viewHolder)
            }

            is CheeseListItem -> {
                setCheesePresenter(item, cardView, viewHolder)
            }
        }
        if (item == null) {
            setNullPresenter(cardView, viewHolder)
        }
        Log.d("TAG", "onBindViewHolder")
    }

    private fun setNullPresenter(cardView: ImageCardView, viewHolder: ViewHolder) {
//        if (movie.cardImageUrl != null) {
//            cardView.titleText = movie.title
//            cardView.contentText = movie.studio
        cardView.titleText = ""
        cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)
        Glide.with(viewHolder.view.context)
            .load(IMAGE_URL)
            .centerCrop()
            .error(mDefaultCardImage)
            .into(cardView.mainImageView)
//        }
    }

    private fun setMoviePresenter(movie: Movie, cardView: ImageCardView, viewHolder: ViewHolder) {
        if (movie.cardImageUrl != null) {
            cardView.titleText = movie.title
            cardView.contentText = movie.studio
            cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)
            Glide.with(viewHolder.view.context)
                .load(movie.cardImageUrl)
                .centerCrop()
                .error(mDefaultCardImage)
                .into(cardView.mainImageView)
        }
    }

    private fun setCheesePresenter(
        cheeseListItem: CheeseListItem,
        cardView: ImageCardView,
        viewHolder: ViewHolder
    ) {
//        if (cheese.cardImageUrl != null) {
        cardView.titleText = cheeseListItem.name
        cardView.contentText = cheeseListItem.name
        cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)
        Glide.with(viewHolder.view.context)
            .load(MoviePagingSource.ALTERNATE_IMAGE_URL)
            .centerCrop()
            .error(mDefaultCardImage)
            .into(cardView.mainImageView)
//    }
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        Log.d(TAG, "onUnbindViewHolder")
        val cardView = viewHolder.view as ImageCardView
        // Remove references to images so that the garbage collector can free up memory
        cardView.badgeImage = null
        cardView.mainImage = null
    }

    private fun updateCardBackgroundColor(view: ImageCardView, selected: Boolean) {
        val color = if (selected) sSelectedBackgroundColor else sDefaultBackgroundColor
        view.setBackgroundColor(color)
        view.setInfoAreaBackgroundColor(color)
    }

    companion object {
        private val TAG = "CardPresenter"

        private val CARD_WIDTH = 313
        private val CARD_HEIGHT = 176
    }
}