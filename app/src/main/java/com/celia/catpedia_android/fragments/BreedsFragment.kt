package com.celia.catpedia_android.fragments

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
import com.celia.catpedia_android.adapters.BreedsAdapter
import com.celia.catpedia_android.databinding.FragmentBreedsBinding
import com.celia.catpedia_android.models.Breed
import com.celia.catpedia_android.persistence.AppBreedsDataBase
import com.celia.catpedia_android.viewmodels.BreedListViewModel
import kotlinx.android.synthetic.main.fragment_breeds.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.launch


class BreedsFragment : Fragment() {

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private lateinit var viewModel: BreedListViewModel

    private lateinit var binding: FragmentBreedsBinding
    private val breedList = mutableListOf<Breed>()
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
        return Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    private suspend fun getBreeds(): List<Breed> {
        val breeds: List<Breed>
        withContext(Dispatchers.IO) {
            val call = getRetrofit().create(APIService::class.java)
                .getCatsBreeds("v1/breeds")
                .execute()
            breeds = call.body()!!
            saveBreedsDataBase(breeds)
            if (!call.isSuccessful) {
                showError()
            }
        }
        return breeds
    }

    //Change all
    private fun showError() {
        Toast.makeText(activity, "Ha ocurrido un error", Toast.LENGTH_SHORT)
            .show()
    }

    fun saveBreedsDataBase(breeds: List<Breed>) {
        val database = AppBreedsDataBase.getAppDatabase(requireContext()).breedDao()
        breeds.forEach{ breed ->
            database.insertAll(breed)
        }
    }

    fun returnBreedsDataBase(): List<Breed> {
        val database = AppBreedsDataBase.getAppDatabase(requireContext()).breedDao()
        return database.getAll()
    }
}