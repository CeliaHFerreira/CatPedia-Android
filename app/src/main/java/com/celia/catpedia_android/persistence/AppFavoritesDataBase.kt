package com.celia.catpedia_android.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.celia.catpedia_android.models.Breed


@Database(
    entities = [
        Breed::class
    ], version = 1, exportSchema = false
)
@TypeConverters(value = [ListBreedConverter::class])
abstract class AppFavoritesDataBase: RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao

    companion object {
        @Volatile
        var INSTANCE: AppFavoritesDataBase? = null

        fun getAppDatabase(context: Context): AppFavoritesDataBase =
            INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        AppFavoritesDataBase::class.java,
                        "favoritesBreeds"
                    )
                        .allowMainThreadQueries()
                        .build()
                INSTANCE = instance
                instance
            }
    }
}