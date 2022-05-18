package com.celia.catpedia_android.models

import com.google.gson.annotations.SerializedName

data class BreedDetail(
    @SerializedName("breeds") var breeds: List<Breed>,
    @SerializedName("id") var id: String,
    @SerializedName("url") var url: String,
    @SerializedName("width") var width: Int,
    @SerializedName("height") var height: Int
)
