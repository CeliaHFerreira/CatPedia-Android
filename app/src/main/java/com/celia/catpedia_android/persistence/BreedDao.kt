package com.celia.catpedia_android.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.celia.catpedia_android.models.Breed

@Dao
interface BreedDao {
    @Query("SELECT * FROM breed")
    fun getAll(): List<Breed>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg breeds: Breed)
}