package com.celia.catpedia_android.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import com.celia.catpedia_android.APIService
import com.celia.catpedia_android.databinding.ActivityBreedDetailBinding
import com.celia.catpedia_android.models.BreedDetail
import kotlinx.android.synthetic.main.activity_breed_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BreedDetailActivity : AppCompatActivity() {

    private val args: BreedDetailActivityArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBreedDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch(Dispatchers.Main) {
            val breed = getBreedDetail(args.breedId)[0].breeds[0]

            detail_breed_title.text = breed.name
            detail_origin.text = breed.origin
            detail_temperament.text = breed.temperament
            detail_lifeSpan.text = breed.lifeSpan
            detail_description.text = breed.description
        }

        goBackButton.setOnClickListener {
            returnToList(it)
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private suspend fun getBreedDetail(id: String): List<BreedDetail> {
        val breed: List<BreedDetail>
        withContext(Dispatchers.IO) {
            val call = getRetrofit().create(APIService::class.java)
                .getBreedDetail("/v1/images/search?breed_ids=$id&limit=4")
                .execute()
            breed = call.body()!!
            if (!call.isSuccessful) {
                showError()
            }
        }
        return breed
    }

    private fun returnToList(view: View) {
        val homeIntent = Intent(this, HomeActivity::class.java)
        startActivity(homeIntent)
    }

    //Change all
    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT)
            .show()
    }
}