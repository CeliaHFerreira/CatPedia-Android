package com.celia.catpedia_android

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.celia.catpedia_android.databinding.ActivityHomeBinding
import com.celia.catpedia_android.fragments.BreedsFragment
import com.celia.catpedia_android.fragments.FavoritesFragment
import com.celia.catpedia_android.models.Breed
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var adapter: BreedsAdapter
    private val breedList = mutableListOf<Breed>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navigation.setOnNavigationItemSelectedListener {
            selectFragment(it)
            true
        }

        binding.navigation.selectedItemId = R.id.navigation_home

        val bundle = intent.extras
        val email = bundle?.getString("email")

        setup()

        val preferences = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        preferences.putString("email", email)
        preferences.apply()

        //getBreeds()

    }

    private fun setup() {
        title = "Home page"
        logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            returnToLogin()
        }
    }

    private fun selectFragment(it: MenuItem) {

        val fragmentClicked: Fragment

        when (it.itemId) {
            R.id.navigation_home -> fragmentClicked = BreedsFragment.newInstance()
            else -> fragmentClicked = FavoritesFragment.newInstance()
        }

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.homeContent, fragmentClicked)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.commit()

    }


    /*private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }*/

    /* private fun getBreeds() {
         CoroutineScope(Dispatchers.IO).launch {
             val call = getRetrofit().create(APIService::class.java)
                 .getCatsBreeds("v1/breeds")
                 .execute()
             val breeds = call.body()
             runOnUiThread {
                 if (call.isSuccessful) {
                     breeds?.let { showBreeds(it) }
                 } else {
                     showError()
                 }
             }
         }
     }*/

    /*private fun showBreeds(breeds: List<Breed>) {
        breedList.clear()
        breedList.addAll(breeds)
        adapter = BreedsAdapter(breedList)
        binding.rvBreeds.setHasFixedSize(true)
        binding.rvBreeds.layoutManager = LinearLayoutManager(this)
        binding.rvBreeds.adapter = adapter
    }*/

    private fun returnToLogin() {
        val preferences = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        preferences.clear()
        preferences.apply()
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
    }

    public fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT)
            .show()
    }
}


