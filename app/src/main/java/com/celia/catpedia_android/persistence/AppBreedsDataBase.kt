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
abstract class AppBreedsDataBase: RoomDatabase() {

    abstract fun breedDao(): BreedDao

    companion object {
        @Volatile
        var INSTANCE: AppBreedsDataBase? = null

        fun getAppDatabase(context: Context): AppBreedsDataBase =
            INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        AppBreedsDataBase::class.java,
                        "databaseBreeds"
                    )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
                instance
            }
    }
}