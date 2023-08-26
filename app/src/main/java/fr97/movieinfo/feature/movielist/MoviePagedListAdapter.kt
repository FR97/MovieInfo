package fr97.movieinfo.feature.movielist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr97.movieinfo.R
import fr97.movieinfo.data.api.ApiConstants.IMAGE_SIZE_SMALL
import fr97.movieinfo.data.api.ApiConstants.IMAGE_URL_BASE
import fr97.movieinfo.databinding.ListItemMovieBinding
import java.lang.IllegalStateException

class MoviePagedListAdapter(val onClickListener: (MovieListItemModel) -> Unit) :
    PagedListAdapter<MovieListItemModel, MoviePagedListAdapter.MoviePagedViewHolder>(DIFF_CALLBACK) {
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): MoviePagedViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val movieItemBinding: ListItemMovieBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.list_item_movie, parent, false
        )
        return MoviePagedViewHolder(movieItemBinding)
    }

    override fun onBindViewHolder(@NonNull holder: MoviePagedViewHolder, position: Int) {
        holder.bind(getItem(position) ?: throw IllegalStateException("Movie cant be null"))
    }

    inner class MoviePagedViewHolder(private val movieItemBinding: ListItemMovieBinding) :
        RecyclerView.ViewHolder(movieItemBinding.getRoot()), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }
        internal fun bind(movie: MovieListItemModel) {
            val thumbnail = IMAGE_URL_BASE + IMAGE_SIZE_SMALL + movie.thumbnailPath
            Glide.with(itemView.context)
                .load(thumbnail)
                .placeholder(R.drawable.ic_animated_loading_circle)
                .error(R.drawable.image_not_found)
                .into(movieItemBinding.imageViewThumbnail)
            movieItemBinding.textViewTitle.text = movie.title
        }

        override fun onClick(v: View) {
            val adapterPosition = adapterPosition
            val movie = getItem(adapterPosition) ?: throw IllegalStateException("Movie shouldn't be null")
            onClickListener(movie)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieListItemModel>() {
            override fun areItemsTheSame(oldItem: MovieListItemModel, newItem: MovieListItemModel): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: MovieListItemModel, newItem: MovieListItemModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}

