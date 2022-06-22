package com.celia.catpedia_android.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.celia.catpedia_android.R
import com.celia.catpedia_android.adapters.FavoritesAdapter
import com.celia.catpedia_android.databinding.FragmentFavoriteListBinding
import com.celia.catpedia_android.models.Breed
import com.celia.catpedia_android.persistence.AppFavoritesDataBase
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.profile_button.view.*
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
        binding.emptyList.visibility = View.VISIBLE
        addFavorites()
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
            if (favoriteList.isEmpty()) {
                binding.emptyList.visibility = View.VISIBLE
            } else {
                binding.emptyList.visibility = View.GONE
            }
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

    private fun setDarkMode() {
    //change darkMode to true
    val prefs = activity?.getSharedPreferences("visual", Context.MODE_PRIVATE)?.edit()
    val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (AppCompatDelegate.MODE_NIGHT_YES == AppCompatDelegate.getDefaultNightMode() ||
            nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            prefs?.putBoolean("darkmode", false)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            prefs?.putBoolean("darkmode", true)
        }
        prefs?.apply()
    }

    private fun updatePreferences(ascending: Boolean) {
        val prefs = activity?.getSharedPreferences("favorites", Context.MODE_PRIVATE)?.edit()
        prefs?.putBoolean("ascending", ascending)
        prefs?.apply()
    }

    private fun addFavorites() {
        binding.emptyList.tvBtnProfileButton.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING)
            val ft = activity?.supportFragmentManager?.beginTransaction()
            if (ft != null) {
                ft.replace(R.id.nav_host_fragment_container, BreedsFragment.newInstance())
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                ft.commit()
                val mBottomNavigationView = activity?.findViewById(R.id.navigation) as BottomNavigationView
                mBottomNavigationView.menu.findItem(R.id.navigation_home).isChecked = true
            }
        }
    }
}