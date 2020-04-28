package com.zubair.assesment.detail.review

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zubair.assesment.R
import com.zubair.assesment.model.TMDBResultReview
import kotlinx.android.synthetic.main.review_item.view.*

class ReviewRVAdapter(private val item: ArrayList<TMDBResultReview>) :
    RecyclerView.Adapter<ReviewRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(item[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(it: TMDBResultReview) {
            itemView.review_author.text = it.author
            itemView.review_content.text = it.content
        }
    }
}