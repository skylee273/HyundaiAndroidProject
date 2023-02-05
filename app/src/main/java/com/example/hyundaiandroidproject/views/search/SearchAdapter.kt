package com.example.hyundaiandroidproject.views.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.example.hyundaiandroidproject.R
import com.example.hyundaiandroidproject.api.model.MovieEntity
import androidx.recyclerview.widget.RecyclerView
import com.example.hyundaiandroidproject.databinding.ItemRepositoryBinding

class SearchAdapter(private val itemClick : (MovieEntity) -> Unit) :
    ListAdapter<MovieEntity, SearchAdapter.ViewHolder>(
        diffUtil
    ) {

    class ViewHolder(private val binding: ItemRepositoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieEntity) {
            binding.movie = movie
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRepositoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_repository,
            parent,
            false
        )
        return ViewHolder(binding).apply {
            binding.root.setOnClickListener {
                val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION }
                    ?: return@setOnClickListener
                itemClick(getItem(position))
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MovieEntity>() {
            override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity) =
                oldItem.title == newItem.title
        }
    }


}