package com.example.hyundaiandroidproject.views.search

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hyundaiandroidproject.R
import com.example.hyundaiandroidproject.api.model.MovieEntity

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.RepositoryHolder>() {

    // 초깃값을 빈 ArrayList 할당
    var items: MutableList<MovieEntity> = mutableListOf()

    private val placeholder = ColorDrawable(Color.GRAY)

    @Nullable
    private var listener: ItemClickListener? = null

    inner class RepositoryHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repository, parent, false)
    ) {
        var ivProfile: ImageView = itemView.findViewById(R.id.ivItemRepositoryProfile)
        var tvName: TextView = itemView.findViewById(R.id.tvItemRepositoryName)
        var tvLanguage: TextView = itemView.findViewById(R.id.tvItemRepositoryLanguage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryHolder =
        RepositoryHolder(parent);


    override fun onBindViewHolder(holder: RepositoryHolder, position: Int) {
        // let() 함수를 사용하여 값이 사용되는 범위를 한정한다.
        items[position].let { repo ->
            with(holder.itemView) {
                Glide.with(context)
                    .load(repo.image)
                    .placeholder(placeholder)
                    .into(holder.ivProfile)
                holder.tvName.text = repo.title
                holder.tvLanguage.text = repo.subtitle
                    if (TextUtils.isEmpty(repo.subtitle)) holder.itemView.context.getText(R.string.no_language_specified) else repo.subtitle
                listener?.onItemClick(repo)
            }

        }
    }

    // 항상 크기만 반환되기 때문에 단일 표현식 가능
    override fun getItemCount(): Int = items.size

    fun setMovieItems(movieItems: List<MovieEntity>) {
        this.items = movieItems.toMutableList()
    }

    fun clearItems() {
        items.clear()
    }

    fun setItemClickListener(listener: ItemClickListener?) {
        this.listener = listener
    }

    interface ItemClickListener {
        fun onItemClick(repository: MovieEntity?)
    }
}