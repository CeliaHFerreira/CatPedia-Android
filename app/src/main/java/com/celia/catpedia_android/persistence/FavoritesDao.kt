package com.celia.catpedia_android.persistence

import androidx.room.*
import com.celia.catpedia_android.models.Breed

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM breed")
    fun getFavorites(): List<Breed>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(vararg breeds: Breed)

    @Query("DELETE FROM breed WHERE id = :id")
    fun deleteFromFavorites(id: String)
}