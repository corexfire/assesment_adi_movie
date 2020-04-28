package com.zubair.assesment.detail.trailer

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zubair.assesment.R
import com.zubair.assesment.api.MAX_RES_YT_IMAGE
import com.zubair.assesment.api.YT_IMAGE_URL
import com.zubair.assesment.model.TMDBResult
import kotlinx.android.synthetic.main.credits_item.view.*


class TrailerRVAdapter(private val item: ArrayList<TMDBResult>) :
    RecyclerView.Adapter<TrailerRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.credits_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(item[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(it: TMDBResult) {
            val profileUrl: String = YT_IMAGE_URL + it.key + MAX_RES_YT_IMAGE
            Glide.with(itemView.context)
                .load(profileUrl)
                .into(itemView.credits_image)
            val videoId = it.key
            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
                intent.putExtra("VIDEO_ID", videoId)
                itemView.context.startActivity(intent)
            }
        }
    }
}