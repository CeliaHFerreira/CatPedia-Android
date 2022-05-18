package com.celia.catpedia_android

import com.celia.catpedia_android.models.Breed
import com.celia.catpedia_android.models.BreedDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

interface APIService {
    @Headers(
        "x-api-key: cd7828b3-f9d5-41ac-8008-ecf17b046d82"
    )
    @GET
    fun getCatsBreeds(@Url url:String): Call<List<Breed>>

    @GET
    fun getBreedDetail(@Url url:String): Call<List<BreedDetail>>
}