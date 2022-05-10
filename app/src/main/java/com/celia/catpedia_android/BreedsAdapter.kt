package com.celia.catpedia_android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BreedsAdapter (private val images: List<String>) : RecyclerView.Adapter<BreedsHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedsHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BreedsHolder(layoutInflater.inflate(R.layout.item_breed, parent, false))
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: BreedsHolder, position: Int) {
        val item = images[position]
        holder.bind(item)
    }

}