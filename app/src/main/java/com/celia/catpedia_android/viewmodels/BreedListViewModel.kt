package com.celia.catpedia_android.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel

class BreedListViewModel : ViewModel() {
    init {
        Log.i("BreedListViewModel", "BreedListViewModel created!")
    }
    override fun onCleared() {
        super.onCleared()
        Log.i("BreedListViewModel", "BreedListViewModel destroyed!")
    }
}
