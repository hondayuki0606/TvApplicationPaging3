package com.example.tvapplicationpaging3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.tvapplicationpaging3.paging.MovieAdapter
import com.example.tvapplicationpaging3.paging.PagingSourceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * paging3のrecyclerViewを使用したFragment
 */
@AndroidEntryPoint
class SubMainFragment : Fragment() {
    private val viewModel: PagingSourceViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sub_main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = MovieAdapter()
        lifecycleScope.launch {
            viewModel.getMoviesAsFlow().collectLatest { value ->
                (recyclerView.adapter as MovieAdapter).submitData(value)
            }
        }
    }
}