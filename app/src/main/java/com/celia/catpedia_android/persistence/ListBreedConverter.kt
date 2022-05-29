package com.celia.catpedia_android.persistence

import androidx.room.TypeConverter
import com.celia.catpedia_android.models.Image
import com.celia.catpedia_android.models.Weight
import com.google.gson.Gson

class ListBreedConverter {
    private val gson = Gson()

    @TypeConverter
    fun StringToWeight(Data: String?): Weight? = gson.fromJson(Data, Weight::class.java)

    @TypeConverter
    fun WeightToString(Data: Weight): String? = gson.toJson(Data)

    @TypeConverter
    fun StringToImage(Data: String?): Image? = gson.fromJson(Data, Image::class.java)

    @TypeConverter
    fun ImageToString(Data: Image?): String? = gson.toJson(Data)
}