package com.celia.catpedia_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.celia.catpedia_android.models.Breed
import com.squareup.picasso.Picasso


class BreedsAdapter(private val breeds: List<Breed>, val itemClickHandler: (Int) -> Unit) : RecyclerView.Adapter<BreedsAdapter.BreedsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedsHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val headerView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_breed, parent, false)
        val headerViewHolder = BreedsHolder(headerView)
        headerView.setOnClickListener {
            itemClickHandler.invoke(headerViewHolder.adapterPosition)
        }
        return BreedsHolder(layoutInflater.inflate(R.layout.item_breed, parent, false))

    }

    override fun getItemCount(): Int = breeds.size

    override fun onBindViewHolder(holder: BreedsHolder, position: Int) {
        val breed = breeds[position]
        holder.tvName.text = breed.name
        holder.tvOrigin.text = breed.origin
        if (!breed.image?.url.isNullOrEmpty()) {
            Picasso.get().load(breed.image?.url).into(holder.ivCat)
        } else {
            holder.ivCat.setImageResource(R.drawable.cat)
        }

        if (breed.favorite) {
            holder.ivLike.setImageResource(R.drawable.ic_favorite)
        } else {
            holder.ivLike.setImageResource(R.drawable.ic_no_favorite)
        }

    }

    class BreedsHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvName: TextView = itemView.findViewById(R.id.nameBreed)
        var tvOrigin: TextView = itemView.findViewById(R.id.originBreed)
        var ivLike: ImageView = itemView.findViewById(R.id.like)
        var ivCat: ImageView = itemView.findViewById(R.id.ivCat)

    }
}