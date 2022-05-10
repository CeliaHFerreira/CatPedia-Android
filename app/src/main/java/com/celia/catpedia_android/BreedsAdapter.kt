package com.celia.catpedia_andrioid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.celia.catpedia_android.R
import com.celia.catpedia_android.models.Breed
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_breed.*

class BreedsAdapter (private val breeds: List<Breed>) : RecyclerView.Adapter<BreedsAdapter.BreedsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedsHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BreedsHolder(layoutInflater.inflate(R.layout.item_breed, parent, false))
    }

    override fun getItemCount(): Int {
        return breeds.size
    }

    override fun onBindViewHolder(holder: BreedsHolder, position: Int) {
        val breed = breeds[position]

        holder.tvName.text = breed.name
        holder.tvOrigin.text = breed.origin
        if (!breed.image?.url.isNullOrEmpty()) {
            Picasso.get().load(breed.image?.url).into(holder.ivCat)
        } else {
            holder.ivCat.setImageResource(R.drawable.cat)
        }
    }

    class BreedsHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.nameBreed)
        var tvOrigin: TextView = itemView.findViewById(R.id.originBreed)
        var ivLike: ImageView = itemView.findViewById(R.id.like)
        var ivCat: ImageView = itemView.findViewById(R.id.ivCat)

    }

}