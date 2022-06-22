package com.celia.catpedia_android.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.celia.catpedia_android.R
import com.celia.catpedia_android.databinding.ActivityHomeBinding
import com.celia.catpedia_android.fragments.BreedsFragment
import com.celia.catpedia_android.fragments.FavoritesFragment
import com.celia.catpedia_android.fragments.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val hBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(hBinding.root)

        val bundle = intent.extras
        val email = bundle?.getString("email")

        val preferences =
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        preferences.putString("email", email)
        preferences.apply()

        hBinding.navigation.setOnItemSelectedListener {
            selectFragment(it)
            true
        }
    }

    private fun selectFragment(it: MenuItem) {
        val fragmentClicked: Fragment

        when (it.itemId) {
            R.id.navigation_home -> fragmentClicked = BreedsFragment.newInstance()
            R.id.navigation_favorite -> fragmentClicked = FavoritesFragment.newInstance()
            else -> fragmentClicked = ProfileFragment.newInstance()
        }

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.nav_host_fragment_container, fragmentClicked)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.commit()
    }

    private fun returnToLogin() {
        val preferences =
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        preferences.clear()
        preferences.apply()
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
    }

}