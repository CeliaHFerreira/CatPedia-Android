package com.celia.catpedia_android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.celia.catpedia_android.APIService
import com.celia.catpedia_android.adapters.BreedsAdapter
import com.celia.catpedia_android.databinding.FragmentBreedsBinding
import com.celia.catpedia_android.models.Breed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.launch


class BreedsFragment : Fragment() {

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            val breeds = getBreeds()
            breedList.addAll(breeds)
            binding.rvBreeds.layoutManager = LinearLayoutManager(activity)
            binding.rvBreeds.adapter = BreedsAdapter(breedList)
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
}