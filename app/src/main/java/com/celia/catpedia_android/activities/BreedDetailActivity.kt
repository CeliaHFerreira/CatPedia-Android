package com.celia.catpedia_android.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import com.celia.catpedia_android.APIService
import com.celia.catpedia_android.adapters.ImageDetailAdapter
import com.celia.catpedia_android.databinding.ActivityBreedDetailBinding
import com.celia.catpedia_android.models.Breed
import com.celia.catpedia_android.models.BreedDetail
import kotlinx.android.synthetic.main.activity_breed_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BreedDetailActivity : AppCompatActivity() {

    private var shortAnimationDuration: Int = 0

    private val args: BreedDetailActivityArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBreedDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.scrollView2.visibility = View.GONE
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
        lifecycleScope.launch(Dispatchers.Main) {

            val breedDetail = getBreedDetail(args.breedId)
            val breed = breedDetail[0].breeds[0]
            selectImages(breedDetail)
            setDetailBreed(breed)
            binding.scrollView2.visibility = View.VISIBLE
            binding.loadingSpinnerDetail.visibility = View.GONE
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

    private fun selectImages(breedDetails: List<BreedDetail>) {
        val images: MutableList<String> = arrayListOf()
        breedDetails.forEach {
            images.add(it.url)
        }
        val urls = ImageDetailAdapter(images)
        breed_image_view_pager.adapter = urls
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


    private fun setDetailBreed(breed: Breed) {
        detail_breed_title.text = breed.name
        detail_origin.text = breed.origin
        detail_temperament.text = breed.temperament
        detail_lifeSpan.text = breed.lifeSpan
        detail_description.text = breed.description
        rbAdaptability.numStars = 5
        rbAdaptability.rating = breed.adaptability.toFloat()
        rbAffection.numStars = 5
        rbAffection.rating = breed.affectionLevel.toFloat()
        rbChild.numStars = 5
        rbChild.rating = breed.childFriendly.toFloat()
        rbDog.numStars = 5
        rbDog.rating = breed.dogFriendly.toFloat()
        rbEnergy.numStars = 5
        rbEnergy.rating = breed.energyLevel.toFloat()
        rbHealthIssues.numStars = 5
        rbHealthIssues.rating = breed.healthIssues.toFloat()
        rbIntelligence.numStars = 5
        rbIntelligence.rating = breed.intelligence.toFloat()
        rbSocial.numStars = 5
        rbSocial.rating = breed.socialNeeds.toFloat()
        rbStrangers.numStars = 5
        rbStrangers.rating = breed.strangerFriendly.toFloat()
        rbVocalisation.numStars = 5
        rbVocalisation.rating = breed.vocalisation.toFloat()
    }

    //Change all
    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT)
            .show()
    }
}