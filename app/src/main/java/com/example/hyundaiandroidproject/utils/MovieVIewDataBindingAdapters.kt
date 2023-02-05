package com.example.hyundaiandroidproject.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hyundaiandroidproject.api.model.MovieEntity
import com.example.hyundaiandroidproject.views.search.SearchAdapter
import com.example.hyundaiandroidproject.views.search.SearchViewModel

@BindingAdapter("endlessScroll")
fun RecyclerView.setEndlessScroll(
    viewModel: SearchViewModel
) {
    val scrollListener =
        object : EndlessRecyclerViewScrollListener(layoutManager as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                // viewModel.requestPagingMovie(totalItemsCount + 1)
            }
        }
    addOnScrollListener(scrollListener)
}


@BindingAdapter("setItems")
fun RecyclerView.setAdapterItems(items: MutableList<MovieEntity>?) {
    items?.let {
        (adapter as SearchAdapter).submitList(it.toMutableList())
    }
}
