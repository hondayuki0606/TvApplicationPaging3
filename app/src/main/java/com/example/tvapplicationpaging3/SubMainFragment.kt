package com.example.tvapplicationpaging3

import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tvapplicationpaging3.paging.MovieAdapter
import com.example.tvapplicationpaging3.paging.PagingSourceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * paging3のrecyclerViewを使用したFragment
 */
@AndroidEntryPoint
class SubMainFragment : Fragment() {
    private val viewModel: PagingSourceViewModel by viewModels()
    private var recyclerView: RecyclerView? = null
    private var button: Button? = null
    private var initPosition = 499
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sub_main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycler_view)
        button = view.findViewById(R.id.button)
        button?.isFocusable = true
        button?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // フォーカスを得たときの処理
                button?.text = "Focused"
                button?.setBackgroundColor(Color.BLUE)
            } else {
                // フォーカスを失ったときの処理
                button?.text = "Not Focused"
                button?.setBackgroundColor(Color.GRAY)
            }
        }
        button?.requestFocus()
        val adapter = MovieAdapter()
        lifecycleScope.launch {
//            viewModel.getMoviesAsFlow().collectLatest { pagingData ->
//                adapter.submitData(pagingData)
//            }
        }
        recyclerView?.adapter = adapter
        adapter.addOnPagesUpdatedListener {
            recyclerView?.scrollToPosition(initPosition)
        }


        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int){
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                    val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                    val middlePosition = (firstVisiblePosition + lastVisiblePosition) / 2

                    // アダプターの選択された位置を更新
                    adapter.updateSelectedPosition(middlePosition)
                }
            }
        })
    }

    fun onKeyDown(keyCode: Int): Boolean {
        var flg = false
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP -> {
                // 上矢印キーが押されたときの処理
                if (recyclerView?.hasFocus() == true) {
                    button?.requestFocus()
                }
                flg = true
            }

            KeyEvent.KEYCODE_DPAD_DOWN -> {
                // 下矢印キーが押されたときの処理
                if (button?.hasFocus() == true) {
//                    recyclerView?.post {
                        val viewHolder = recyclerView?.findViewHolderForAdapterPosition(initPosition)
                        viewHolder?.itemView?.requestFocus()
//                    }
                }
                flg = true
            }

            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                if (button?.hasFocus() == true) {
                    flg = true
                }
            }

            KeyEvent.KEYCODE_DPAD_LEFT -> {
                if (button?.hasFocus() == true) {
                    flg = true
                }
            }

            else -> {
                flg = false
            }
        }
        return flg
    }
}