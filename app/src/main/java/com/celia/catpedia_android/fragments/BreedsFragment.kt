package com.celia.catpedia_android.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.celia.catpedia_android.APIService
import com.celia.catpedia_android.BreedsAdapter
import com.celia.catpedia_android.databinding.FragmentHomeListBinding
import com.celia.catpedia_android.models.Breed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BreedsFragment : Fragment() {

    private lateinit var binding: FragmentHomeListBinding
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
        binding = FragmentHomeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlobalScope.launch(Dispatchers.Main) {
            val breeds = getBreeds()
            breedList.addAll(breeds)
            binding.rvBreeds.layoutManager = LinearLayoutManager(activity)
            binding.rvBreeds.adapter = BreedsAdapter(breedList, this@BreedsFragment::onItemClickHandler)
        }
    }

    private fun onItemClickHandler(position:Int){
        Log.d("***","${position}");
    }

    //Change to other file
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://api.thecatapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    //Change to other file
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