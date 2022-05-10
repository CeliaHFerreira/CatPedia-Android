package com.celia.catpedia_android

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.celia.catpedia_android.databinding.ItemBreedBinding
import com.squareup.picasso.Picasso

class BreedsHolder (view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemBreedBinding.bind(view)

    fun bind(image:String) {
        Picasso.get().load(image).into(binding.itCat)
    }
}