package com.celia.catpedia_android.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.celia.catpedia_android.APIService
import com.celia.catpedia_android.adapters.BreedsAdapter
import com.celia.catpedia_android.databinding.ActivityBreedsBinding
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

class BreedsActivity: AppCompatActivity() {
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private lateinit var viewModel: BreedListViewModel

    private lateinit var binding: ActivityBreedsBinding
    private val breedList = mutableListOf<Breed>()
    private var countNumberOfDataCall = 0

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreedsBinding.inflate(layoutInflater)
        binding.svBreeds.visibility = View.GONE
        binding.srBreeds.visibility = View.GONE
        setContentView(binding.root)
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
            binding.rvBreeds.layoutManager = LinearLayoutManager(this@BreedsActivity)
            binding.rvBreeds.adapter = BreedsAdapter(breedList)
            swipeRefreshLayout?.isRefreshing = false
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private suspend fun getBreeds(): List<Breed> {
        var breeds: List<Breed>
            withContext(Dispatchers.IO) {
                val call = getRetrofit().create(APIService::class.java)
                    .getCatsBreeds("v1/breeds")
                    .execute()
                breeds = call.body()!!
                countNumberOfDataCall++
                if (!call.isSuccessful) {
                    showError()
                }
            }

        return breeds
    }
    //Change all
    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT)
            .show()
    }
/*
    private fun saveBreedsDataBase(breeds: List<Breed>) {
        val database = AppBreedsDataBase.getAppDatabase(Context.MODE_PRIVATE).breedDao()
        breeds.forEach{ breed ->
            database.insertAll(breed)
        }
        updateCountSharedPreferences()
    }*/

    /*private fun returnBreedsDataBase(): List<Breed> {
        val database = AppBreedsDataBase.getAppDatabase(requireContext(Context.MODE_PRIVATE)).breedDao()
        updateCountSharedPreferences()
        return database.getAll()
    }
*/
    /*private fun updateCountSharedPreferences() {
        val prefs = this?.getSharedPreferences("breeds", MODE_PRIVATE) ?: return
        with (prefs.edit()) {
            putInt("breeds", countNumberOfDataCall)
            apply()
        }
    }*/

    /*private fun retrieveFavoritesBreeds(breed: List<Breed>): List<Breed> {
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
    }*/

}