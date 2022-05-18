package com.celia.catpedia_android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.celia.catpedia_android.R
import com.celia.catpedia_android.databinding.ItemBreedBinding
import com.celia.catpedia_android.fragments.BreedsFragmentDirections
import com.celia.catpedia_android.models.Breed
import com.squareup.picasso.Picasso


private typealias BreedListener = (Breed) -> Unit

class BreedsAdapter(private val breeds: MutableList<Breed>, private val listener: BreedListener) :
    RecyclerView.Adapter<BreedsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val breedsView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_breed, parent, false)
        return ViewHolder(breedsView, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(breeds[position])
    }

    override fun getItemCount(): Int = breeds.size

    class ViewHolder(view: View, private val listener: BreedListener) :
        RecyclerView.ViewHolder(view) {
        private val binding = ItemBreedBinding.bind(view)
        fun bind(breed: Breed) {
            with(binding) {
                nameBreed.text = breed.name
                originBreed.text = breed.origin
                if (!breed.image?.url.isNullOrEmpty()) {
                    Picasso.get().load(breed.image?.url).into(ivCat)
                } else {
                    ivCat.setImageResource(R.drawable.cat)
                }
                if (breed.favorite) {
                    like.setImageResource(R.drawable.ic_favorite)
                } else {
                    like.setImageResource(R.drawable.ic_no_favorite)
                }

                root.setOnClickListener {
                    breed.let { breed ->
                        navigateToBreed(breed, it)
                    }
                }
            }
        }

        private fun navigateToBreed(
            breed: Breed,
            view: View
        ) {
            val action = BreedsFragmentDirections.actionBreedsFragmentToBreedDetailActivity(breed.id)
            view.findNavController().navigate(action)
        }
    }
}