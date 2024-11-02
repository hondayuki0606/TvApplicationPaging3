package com.example.tvapplicationpaging3

//import com.example.tvapplicationpaging3.MovieList.list

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.paging.PagingDataAdapter
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.HorizontalGridView
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.OnItemViewSelectedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import androidx.leanback.widget.VerticalGridView
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.tvapplicationpaging3.paging.PagingSourceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import javax.annotation.Nonnull


/**
 * Loads a grid of cards with movies to browse.
 */
@AndroidEntryPoint
class MainFragment : RowsSupportFragment() {

    private val mHandler = Handler(Looper.myLooper()!!)
    private lateinit var mBackgroundManager: BackgroundManager
    private var mDefaultBackground: Drawable? = null
    private lateinit var mMetrics: DisplayMetrics
    private var mBackgroundTimer: Timer? = null
    private var mBackgroundUri: String? = null
    private val viewModel: PagingSourceViewModel by viewModels()
    val movieAdapter: PagingDataAdapter<Movie> = PagingDataAdapter(CardPresenter(),
        object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(
                oldItem: Movie,
                newItem: Movie
            ): Boolean {
                return oldItem.imageId == newItem.imageId
            }

            override fun areContentsTheSame(
                oldItem: Movie,
                newItem: Movie
            ): Boolean {
                return oldItem == newItem
            }
        })
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onActivityCreated(savedInstanceState)

        prepareBackgroundManager()

//        setupUIElements()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        movieAdapter.setHasStableIds(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadRows()
        // スクロール位置を更新
        verticalGridView.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
//                viewModel.scrollPosition = (recyclerView.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
            }
        })
//        // スクロール位置を更新
//       horizontalGridView.setOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                viewModel.scrollPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//            }
//        })
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

//    private fun setupUIElements() {
//        title = getString(R.string.browse_title)
//        // over title
//        headersState = BrowseSupportFragment.HEADERS_ENABLED
//        isHeadersTransitionOnBackEnabled = true
//
//        // set fastLane (or headers) background color
//        brandColor = ContextCompat.getColor(requireContext(), R.color.fastlane_background)
//        // set search icon color
//        searchAffordanceColor = ContextCompat.getColor(requireContext(), R.color.search_opaque)
//    }



    private fun loadRows() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val header = HeaderItem(0, "MovieList.MOVIE_CATEGORY[i]")
        movieAdapter.setHasStableIds(true)
        rowsAdapter.add(ListRow(header, movieAdapter))
//        adapter = movieAdapter

        lifecycleScope.launch {
            viewModel.getMoviesAsFlow().collectLatest { value ->
                movieAdapter.submitData(value)
                val rowViewHolder =  getRowViewHolder(0)
//                val horizontalGridView = rowViewHolder?.findViewById<HorizontalGridView>(R.id.horizontalGridView)

            }
//            movieAdapter.loadStateFlow.collect { loadState ->
//                val isListEmpty =
//                    loadState.refresh is LoadState.NotLoading && movieAdapter.size() == 0
////                // show empty list
////                emptyList.isVisible = isListEmpty
////                // Only show the list if refresh succeeds.
////                list.isVisible = !isListEmpty
//            }
        }

        movieAdapter.addLoadStateListener { combinedLoadStates ->
            if (combinedLoadStates.refresh !is LoadState.NotLoading) {
//                return Unit // this is the void equivalent in kotlin
            }
//            val presenter = movieAdapter.presenterSelector
//            val size = movieAdapter.size()
//            val source = combinedLoadStates.source
//            val append = combinedLoadStates.append
//            val refresh = combinedLoadStates.refresh
//            Log.d("test", "size = $size")
//            Log.d("test", "source = $source")
//            Log.d("test", "append = $append")
//            Log.d("test", "refresh = $refresh")
            Log.d("test", "combinedLoadStates.refresh = ${combinedLoadStates.refresh}")
        }
       val s = (rowsAdapter as ArrayObjectAdapter)
        val rows = s.get(0)

//        rows.
        adapter = rowsAdapter
//        adapter?.getR
    }



    private fun setupEventListeners() {
//        setOnSearchClickedListener {
//            Toast.makeText(requireContext(), "Implement your own in-app search", Toast.LENGTH_LONG)
//                .show()
//        }
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

            if (item is Movie) {
//                movieAdapter.notifyItemRangeChanged(0,5)
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
//
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

    companion object {
        private val TAG = "MainFragment"

        private val BACKGROUND_UPDATE_DELAY = 300
    }
}