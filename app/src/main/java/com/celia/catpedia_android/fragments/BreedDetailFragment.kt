package com.celia.catpedia_android.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.celia.catpedia_android.models.Breed

class BreedDetailFragment : Fragment() {
    private val breed: Breed? = null

    companion object {
        fun newInstance(breed: Breed?): BreedDetailFragment {
            val breedDetailFragment = BreedDetailFragment()
            val args = Bundle()
            //args.putParcelable("breed", breed);
            //BreedDetailFragment.setArguments(args);
            return breedDetailFragment
        }
    }
}