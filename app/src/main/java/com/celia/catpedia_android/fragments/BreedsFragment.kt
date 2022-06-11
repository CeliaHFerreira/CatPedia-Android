package com.celia.catpedia_android.fragments

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.celia.catpedia_android.APIService
import com.celia.catpedia_android.BuildConfig
import com.celia.catpedia_android.adapters.BreedsAdapter
import com.celia.catpedia_android.databinding.FragmentBreedsBinding
import com.celia.catpedia_android.models.Breed
import com.celia.catpedia_android.persistence.AppBreedsDataBase
import com.celia.catpedia_android.persistence.AppFavoritesDataBase
import com.celia.catpedia_android.viewmodels.BreedListViewModel
import kotlinx.android.synthetic.main.fragment_breeds.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class BreedsFragment : Fragment() {

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private lateinit var viewModel: BreedListViewModel

    private lateinit var binding: FragmentBreedsBinding
    private val breedList = mutableListOf<Breed>()
    private var countNumberOfDataCall = 0
    companion object {
        fun newInstance(): BreedsFragment {
            return BreedsFragment()
        }
    }

    //Control not call all the time this
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBreedsBinding.inflate(inflater, container, false)
        binding.svBreeds.visibility = View.GONE
        binding.srBreeds.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout = srBreeds
        swipeRefreshLayout?.setOnRefreshListener {
            setData()
        }
        setData()
    }

    private fun setData() {
        breedList.clear()
        lifecycleScope.launch(Dispatchers.Main) {
            val breeds = getBreeds()
            breedList.addAll(breeds)
            binding.svBreeds.visibility = View.VISIBLE
            binding.srBreeds.visibility = View.VISIBLE
            binding.loadingSpinner.visibility = View.GONE
            binding.rvBreeds.layoutManager = LinearLayoutManager(activity)
            binding.rvBreeds.adapter = BreedsAdapter(breedList)
            swipeRefreshLayout?.isRefreshing = false
        }
    }

    private fun getRetrofit(): Retrofit {
        // We simulate different EndPoint without persistence
        return if (BuildConfig.DEBUG) {
            Retrofit.Builder()
                .baseUrl("https://api.thecatapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        } else {
            Retrofit.Builder()
                .baseUrl("https://api.thecatapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }


    private suspend fun getBreeds(): List<Breed> {
        var breeds: List<Breed>
        val prefs = activity?.getSharedPreferences("breeds", MODE_PRIVATE)
        countNumberOfDataCall = if (BuildConfig.DEBUG) 0 else prefs?.getInt("breeds", 0)!! // We simulate different EndPoint without persistence
        if (countNumberOfDataCall % 10 == 0 || countNumberOfDataCall == 0) {
            withContext(Dispatchers.IO) {
                val call = getRetrofit().create(APIService::class.java)
                    .getCatsBreeds("breeds")
                    .execute()
                breeds = call.body() ?: returnBreedsDataBase()
                breeds = retrieveFavoritesBreeds(breeds)
                countNumberOfDataCall++
                saveBreedsDataBase(breeds)
                if (!call.isSuccessful) {
                    showError()
                }
            }
        } else {
            countNumberOfDataCall++
            breeds = retrieveFavoritesBreeds(returnBreedsDataBase())
        }
        return breeds
    }

    //Change all
    private fun showError() {
        Toast.makeText(activity, "Ha ocurrido un error", Toast.LENGTH_SHORT)
            .show()
    }

    private fun saveBreedsDataBase(breeds: List<Breed>) {
        val database = AppBreedsDataBase.getAppDatabase(requireContext()).breedDao()
        breeds.forEach{ breed ->
            database.insertAll(breed)
        }
        updateCountSharedPreferences()
    }

    private fun returnBreedsDataBase(): List<Breed> {
        val database = AppBreedsDataBase.getAppDatabase(requireContext()).breedDao()
        updateCountSharedPreferences()
        return database.getAll()
    }

    private fun updateCountSharedPreferences() {
        val prefs = activity?.getSharedPreferences("breeds", MODE_PRIVATE) ?: return
        with (prefs.edit()) {
            putInt("breeds", countNumberOfDataCall)
            apply()
        }
    }

    private fun retrieveFavoritesBreeds(breed: List<Breed>): List<Breed> {
        val favoritesDataBase = AppFavoritesDataBase.getAppDatabase(requireContext()).favoritesDao()
        val favoritesBreeds = favoritesDataBase.getFavorites()
        favoritesBreeds.forEach { favoriteBreed ->
            breed.forEach { breed ->
                if (breed.id == favoriteBreed.id) {
                    breed.favorite = true
                }
            }
        }
        return breed
    }
}