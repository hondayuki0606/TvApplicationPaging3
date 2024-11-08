package com.example.tvapplicationpaging3

import java.util.Timer
import java.util.TimerTask

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.OnItemViewSelectedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import androidx.core.content.ContextCompat
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.leanback.paging.PagingDataAdapter
import androidx.leanback.widget.ListRowView
import androidx.leanback.widget.ObjectAdapter
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.tvapplicationpaging3.paging.MoviePagingSource
import com.example.tvapplicationpaging3.paging.PagingSourceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Loads a grid of cards with movies to browse.
 */
@AndroidEntryPoint
class MainFragment : BrowseSupportFragment() {

    private val mHandler = Handler(Looper.myLooper()!!)
    private lateinit var mBackgroundManager: BackgroundManager
    private var mDefaultBackground: Drawable? = null
    private lateinit var mMetrics: DisplayMetrics
    private var mBackgroundTimer: Timer? = null
    private var mBackgroundUri: String? = null
    private val viewModel: PagingSourceViewModel by viewModels()
    private var firstLoad = true

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onActivityCreated(savedInstanceState)

        prepareBackgroundManager()

        setupUIElements()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadRows()

        setupEventListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: " + mBackgroundTimer?.toString())
        mBackgroundTimer?.cancel()
    }

    private fun prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(activity)
        mBackgroundManager.attach(requireActivity().window)
        mDefaultBackground =
            ContextCompat.getDrawable(requireContext(), R.drawable.default_background)
        mMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(mMetrics)
    }

    private fun setupUIElements() {
        title = getString(R.string.browse_title)
        // over title
        headersState = BrowseSupportFragment.HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        // set fastLane (or headers) background color
        brandColor = ContextCompat.getColor(requireContext(), R.color.fastlane_background)
        // set search icon color
        searchAffordanceColor = ContextCompat.getColor(requireContext(), R.color.search_opaque)
    }

    val cardPresenter = CardPresenter()
    val movieAdapter: PagingDataAdapter<Movie> = PagingDataAdapter(cardPresenter,
        object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(
                oldItem: Movie,
                newItem: Movie
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Movie,
                newItem: Movie
            ): Boolean {
                return oldItem == newItem
            }
        })

    private fun loadRows() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val header = HeaderItem(0, "MovieList.MOVIE_CATEGORY[i]")
        rowsAdapter.add(ListRow(header, movieAdapter))
        lifecycleScope.launch {
            // 最初に仮データを表示
            viewModel.load()
            viewModel.pagingDataFlow.collectLatest { pagingData ->
                movieAdapter.submitData(pagingData)
            }

//            viewModel.uiState.collectLatest {
//                movieAdapter.submitData(
//                    PagingData.from(
//                        listOf(
//                            Movie(
//                                title = "test",
//                                description = "description",
//                                cardImageUrl = MoviePagingSource.IMAGE_URL,
//                                backgroundImageUrl = MoviePagingSource.ALTERNATE_IMAGE_URL,
//                            ),
//                            Movie(
//                                title = "test",
//                                description = "description",
//                                cardImageUrl = MoviePagingSource.IMAGE_URL,
//                                backgroundImageUrl = MoviePagingSource.ALTERNATE_IMAGE_URL,
//                            )
//                        )
//                    )
//                )
//                when (val state = viewModel.uiState.value) {
//                    is UIState.Loading -> {
//                        Log.d("", "honda Loading")
//                    }
//
//                    is UIState.Success -> {
//                        movieAdapter.addLoadStateListener {
//                            Log.d("", "honda addLoadStateListener state$it ")
//                        }
////                        movieAdapter.retry()
//                        movieAdapter.registerObserver(object : ObjectAdapter.DataObserver() {
//                            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
//                                super.onItemRangeInserted(positionStart, itemCount)
//                                // 新しいアイテムが追加されたときの処理
//                                val item = movieAdapter.get(positionStart)
//                                Log.d(
//                                    "",
//                                    "honda onItemRangeInserted item title =${item?.title}, positionStart$positionStart itemCount$itemCount"
//                                )
//                            }
//
//                            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
//                                super.onItemRangeRemoved(positionStart, itemCount)
//                                // アイテムが削除されたときの処理
//                                Log.d("", "honda onItemRangeRemoved")
//                            }
//                        })
//                        Log.d("", "honda Success")
//                        movieAdapter.submitData(state.data)
//                        // これにより、PagingDataの更新がある度にMovieAdapterに通知される
//
//                    }
//
//                    is UIState.Error -> {
//                        Log.d("", "honda Error")
//                    }
//                }
//
//            }
//            movieAdapter.loadStateFlow.collect { loadState->
//                Log.d("", "honda loadStateFlowcollect")
//                when (loadState.refresh) {
//                    is LoadState.Loading -> {
//                        // 初期ロード中のUI表示
//                        Log.d("", "honda CircularProgressIndicator")
////                        ()
//                    }
//                    is LoadState.Error -> {
//                        // エラー時のUI表示
//                        Log.d("", "honda エラー時のUI表示")
////                        Text("Error: ${(loadState.refresh as LoadState.Error).error.message}")
//                    }
//                    else -> {
//                        // 他の状態
//                        Log.d("", "honda 他の状態")
//                    }
//                }
//            }

        }

        adapter = rowsAdapter
    }

    private fun setupEventListeners() {
        setOnSearchClickedListener {
            Toast.makeText(requireContext(), "Implement your own in-app search", Toast.LENGTH_LONG)
                .show()
        }

        onItemViewClickedListener = ItemViewClickedListener()
        onItemViewSelectedListener = ItemViewSelectedListener()
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder,
            item: Any,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {
movieAdapter.refresh()
            if (item is Movie) {
                Log.d(TAG, "Item: " + item.toString())
//                val intent = Intent(context!!, DetailsActivity::class.java)
//                intent.putExtra(DetailsActivity.MOVIE, item)
//
//                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                    activity!!,
//                    (itemViewHolder.view as ImageCardView).mainImageView,
//                    DetailsActivity.SHARED_ELEMENT_NAME
//                )
//                    .toBundle()
//                startActivity(intent, bundle)
            } else if (item is String) {
                if (item.contains(getString(R.string.error_fragment))) {
                    val intent = Intent(context!!, BrowseErrorActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(context!!, item, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private inner class ItemViewSelectedListener : OnItemViewSelectedListener {
        override fun onItemSelected(
            itemViewHolder: Presenter.ViewHolder?, item: Any?,
            rowViewHolder: RowPresenter.ViewHolder, row: Row
        ) {
            if (item != null && firstLoad) {
                firstLoad = false
//                (rowViewHolder.view as ListRowView).gridView.scrollToPosition(498)
            }
            if (item is Movie) {
                mBackgroundUri = item.backgroundImageUrl
                startBackgroundTimer()
            }
        }
    }

    private fun updateBackground(uri: String?) {
        val width = mMetrics.widthPixels
        val height = mMetrics.heightPixels
        Glide.with(requireContext())
            .load(uri)
            .centerCrop()
            .error(mDefaultBackground)
            .into<SimpleTarget<Drawable>>(
                object : SimpleTarget<Drawable>(width, height) {
                    override fun onResourceReady(
                        drawable: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        mBackgroundManager.drawable = drawable
                    }
                })
        mBackgroundTimer?.cancel()
    }

    private fun startBackgroundTimer() {
        mBackgroundTimer?.cancel()
        mBackgroundTimer = Timer()
        mBackgroundTimer?.schedule(UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY.toLong())
    }

    private inner class UpdateBackgroundTask : TimerTask() {

        override fun run() {
            mHandler.post { updateBackground(mBackgroundUri) }
        }
    }

    private inner class GridItemPresenter : Presenter() {
        override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
            val view = TextView(parent.context)
            view.layoutParams = ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT)
            view.isFocusable = true
            view.isFocusableInTouchMode = true
            view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.default_background))
            view.setTextColor(Color.WHITE)
            view.gravity = Gravity.CENTER
            return Presenter.ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
            (viewHolder.view as TextView).text = item as String
        }

        override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {}
    }

    companion object {
        private val TAG = "MainFragment"

        private val BACKGROUND_UPDATE_DELAY = 300
        private val GRID_ITEM_WIDTH = 200
        private val GRID_ITEM_HEIGHT = 200
        private val NUM_ROWS = 6
        private val NUM_COLS = 15
    }
}