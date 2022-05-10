package com.celia.catpedia_android

import com.celia.catpedia_android.models.Breed
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

interface APIService {
    @Headers(
        "x-api-key: "
    )
    @GET
    fun getCatsBreeds(@Url url:String): Call<List<Breed>>

}