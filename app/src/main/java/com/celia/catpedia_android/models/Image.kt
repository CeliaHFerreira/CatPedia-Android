package com.celia.catpedia_android.models

import com.google.gson.annotations.SerializedName

data class Image(

    @SerializedName("id") var id: String,
    @SerializedName("url") var url: String

)