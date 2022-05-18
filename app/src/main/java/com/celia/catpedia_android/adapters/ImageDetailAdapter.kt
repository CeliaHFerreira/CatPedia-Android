package com.celia.catpedia_android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.celia.catpedia_android.R
import com.squareup.picasso.Picasso

class ImageDetailAdapter(private val urls: MutableList<String>) :
    RecyclerView.Adapter<ImageDetailAdapter.ImageDetailViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageDetailViewHolder {
        return ImageDetailViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.image_slider_container, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageDetailViewHolder, position: Int) {
        holder.bind(urls[position])
    }

    override fun getItemCount(): Int = urls.size

    inner class ImageDetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageCat = view.findViewById<ImageView>(R.id.imageSlider)

        fun bind(imageUrl: String) {
            if (!imageUrl.isNullOrEmpty()) {
                Picasso.get().load(imageUrl).into(imageCat)
            }
        }
    }
}