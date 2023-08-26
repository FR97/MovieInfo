package fr97.movieinfo.feature.videoslist

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr97.movieinfo.R
import fr97.movieinfo.data.api.ApiConstants.YT_THUMBNAIL_EXT
import fr97.movieinfo.data.api.ApiConstants.YT_THUMBNAIL_URL
import fr97.movieinfo.data.api.ApiConstants.YT_VIDEO_URL
import fr97.movieinfo.databinding.ListItemVideoBinding

class VideosAdapter(
    private val onVideoClick: (url: String) -> Unit
) : RecyclerView.Adapter<VideoViewHolder>() {

    private val videos: MutableList<VideoModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ListItemVideoBinding =
            DataBindingUtil.inflate(inflater, R.layout.list_item_video, parent, false)
        return VideoViewHolder(binding, onVideoClick)
    }

    override fun getItemCount(): Int = videos.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videos[position])
    }

    fun replaceVideos(newVideos: List<VideoModel>) {
        videos.clear()
        videos.addAll(newVideos)
        notifyDataSetChanged()
    }

}

class VideoViewHolder(
    val binding: ListItemVideoBinding,
    val onVideoClick: (url: String) -> Unit

) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var url: String

    init {
        binding.imageVideoThumbnail.setOnClickListener { onVideoClick(url) }
    }

    @SuppressLint("SetTextI18n")
    fun bind(video: VideoModel) {
        url = "$YT_VIDEO_URL${video.key}"
        val videoThumbnail = "$YT_THUMBNAIL_URL${video.key}$YT_THUMBNAIL_EXT"
        Glide.with(itemView.context)
            .load(videoThumbnail)
            .placeholder(R.drawable.ic_animated_loading_circle)
            .error(R.drawable.image_not_found)
            .into(binding.imageVideoThumbnail)
    }

}

