package com.celia.catpedia_android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.celia.catpedia_android.adapters.BreedsAdapter
import com.celia.catpedia_android.adapters.FavoritesAdapter
import com.celia.catpedia_android.databinding.FragmentFavoriteListBinding
import com.celia.catpedia_android.models.Breed
import com.celia.catpedia_android.persistence.AppFavoritesDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteListBinding
    private val favoriteList = mutableListOf<Breed>()
    companion object {
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteListBinding.inflate(inflater, container, false)
        binding.rvBreeds.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
    }

    private fun setData() {
        favoriteList.clear()
        lifecycleScope.launch(Dispatchers.Main) {
            val breeds = retrieveFavoritesBreeds()
            favoriteList.addAll(breeds)
            binding.rvBreeds.visibility = View.VISIBLE
            binding.rvBreeds.layoutManager = LinearLayoutManager(activity)
            binding.rvBreeds.adapter = FavoritesAdapter(favoriteList)
        }
    }

    private fun retrieveFavoritesBreeds(): List<Breed> {
        val favoritesDataBase = AppFavoritesDataBase.getAppDatabase(requireContext()).favoritesDao()
        return favoritesDataBase.getFavorites()
    }
}