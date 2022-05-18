package com.celia.catpedia_android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.celia.catpedia_android.R

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

}