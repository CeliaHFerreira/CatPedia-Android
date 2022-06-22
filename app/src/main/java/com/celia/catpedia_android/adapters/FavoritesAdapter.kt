package com.celia.catpedia_android.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.celia.catpedia_android.R
import com.celia.catpedia_android.activities.BreedDetailActivity
import com.celia.catpedia_android.databinding.ItemBreedBinding
import com.celia.catpedia_android.models.Breed
import com.celia.catpedia_android.persistence.AppFavoritesDataBase
import com.squareup.picasso.Picasso


class FavoritesAdapter(private val breeds: MutableList<Breed>) :
    RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val FavoritesView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_breed, parent, false)
        return ViewHolder(FavoritesView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(breeds[position])
    }

    override fun getItemCount(): Int = breeds.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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

                like.setOnClickListener {
                    breed.let { breed ->
                        breed.favorite = !breed.favorite
                        if (breed.favorite) {
                            like.setImageResource(R.drawable.ic_favorite)
                        } else {
                            like.setImageResource(R.drawable.ic_no_favorite)
                        }
                        setBreedInFavoriteDatabase(breed, itemView.context)
                    }
                }

                root.setOnClickListener {
                    breed.let { breed ->
                        itemView.context.navigateToBreed(breed, it)
                    }
                }
            }
        }

        private fun Context.navigateToBreed(
            breed: Breed,
            view: View
        ) {
            val intent = Intent(this, BreedDetailActivity::class.java)
            intent.putExtra("breedId", breed.id)
            startActivity(intent)
        }

        private fun setBreedInFavoriteDatabase(breed: Breed, context: Context) {
            val favoriteDatabase = AppFavoritesDataBase.getAppDatabase(context).favoritesDao()
            if (breed.favorite) {
                favoriteDatabase.insertFavorite(breed)
            } else {
                favoriteDatabase.deleteFromFavorites(breed.id)
            }
        }
    }
}