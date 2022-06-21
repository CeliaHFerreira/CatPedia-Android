package com.celia.catpedia_android.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.celia.catpedia_android.R
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
        binding.tbBreeds.inflateMenu(R.menu.toolbar_menu)
        setData()
        binding.tbBreeds.setOnMenuItemClickListener {
            optionMenuSelected(it)
        }
    }

    private fun setData() {
        favoriteList.clear()
        lifecycleScope.launch(Dispatchers.Main) {
            val breeds = retrieveFavoritesBreeds()
            favoriteList.addAll(breeds)
            val prefs = activity?.getSharedPreferences("favorites", Context.MODE_PRIVATE)
            val sorting = prefs?.getBoolean("ascending", true)!!
            if (sorting) {
                favoriteList.sortBy{ it.name }
            } else {
                favoriteList.sortByDescending{ it.name }
            }
            binding.rvBreeds.visibility = View.VISIBLE
            binding.rvBreeds.layoutManager = LinearLayoutManager(activity)
            binding.rvBreeds.adapter = FavoritesAdapter(favoriteList)
        }
    }

    private fun retrieveFavoritesBreeds(): List<Breed> {
        val favoritesDataBase = AppFavoritesDataBase.getAppDatabase(requireContext()).favoritesDao()
        return favoritesDataBase.getFavorites()
    }

    private fun optionMenuSelected(it: MenuItem): Boolean {
        return when (it.itemId) {
            R.id.tbAZ -> {
                sortAscending()
                true
            }
            R.id.tbZA -> {
                sortDescending()
                true
            }
            R.id.tbChange -> {
                setDarkMode()
                true
            }
            else -> {
                false
            }
        }
    }

    private fun sortAscending() {
        favoriteList.sortBy{ it.name }
        binding.rvBreeds.layoutManager = LinearLayoutManager(activity)
        binding.rvBreeds.adapter = FavoritesAdapter(favoriteList)
        updatePreferences(true)
    }

    private fun sortDescending() {
        favoriteList.sortByDescending{ it.name }
        binding.rvBreeds.layoutManager = LinearLayoutManager(activity)
        binding.rvBreeds.adapter = FavoritesAdapter(favoriteList)
        updatePreferences(false)
    }

    fun setDarkMode() {
    //change darkMode to true
    val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (AppCompatDelegate.MODE_NIGHT_YES == AppCompatDelegate.getDefaultNightMode() || nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun updatePreferences(ascending: Boolean) {
        val prefs = activity?.getSharedPreferences("favorites", Context.MODE_PRIVATE)?.edit()
        prefs?.putBoolean("ascending", ascending)
        prefs?.apply()
    }
}