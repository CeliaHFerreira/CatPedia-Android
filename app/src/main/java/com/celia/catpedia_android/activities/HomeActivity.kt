package com.celia.catpedia_android.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.celia.catpedia_android.R
import com.celia.catpedia_android.databinding.ActivityHomeBinding
import com.celia.catpedia_android.fragments.BreedsFragment
import com.celia.catpedia_android.fragments.FavoritesFragment
import com.celia.catpedia_android.fragments.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var hBinding: ActivityHomeBinding
    private lateinit var hActiveFragment: Fragment
    private lateinit var hFragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(hBinding.root)

        selectFragment()

        val bundle = intent.extras
        val email = bundle?.getString("email")

        setup()
        val preferences = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        preferences.putString("email", email)
        preferences.apply()

    }

    private fun selectFragment() {

        hFragmentManager = supportFragmentManager

        val breedsFragment = BreedsFragment()
        val favFragment = FavoritesFragment()
        val profileFragment = ProfileFragment()

        hActiveFragment = breedsFragment

        hFragmentManager.beginTransaction()
                .add(R.id.flContent, favFragment, FavoritesFragment::class.java.name)
                .hide(favFragment).commit()
        hFragmentManager.beginTransaction()
                .add(R.id.flContent, profileFragment, ProfileFragment::class.java.name)
                .hide(profileFragment).commit()
        hFragmentManager.beginTransaction()
                .add(R.id.flContent, breedsFragment, BreedsFragment::class.java.name)
                .commit()

        hBinding.navigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_home -> {
                    hFragmentManager.beginTransaction().hide(hActiveFragment).show(breedsFragment).commit()
                    hActiveFragment = breedsFragment
                    true
                }
                R.id.navigation_favorite -> {
                    hFragmentManager.beginTransaction().hide(hActiveFragment).show(favFragment).commit()
                    hActiveFragment = favFragment
                    true
                }
                R.id.navigation_profile -> {
                    hFragmentManager.beginTransaction().hide(hActiveFragment).show(profileFragment).commit()
                    hActiveFragment = profileFragment
                    true
                }
                else -> false
            }
        }
    }

    private fun setup() {
        title = "Home page"
        logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            returnToLogin()
        }
    }

    private fun returnToLogin() {
        val preferences = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        preferences.clear()
        preferences.apply()
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
    }

}