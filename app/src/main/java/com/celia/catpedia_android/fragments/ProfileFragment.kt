package com.celia.catpedia_android.fragments

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.celia.catpedia_android.R
import com.celia.catpedia_android.activities.BreedDetailActivity
import com.celia.catpedia_android.activities.LoginActivity
import com.celia.catpedia_android.databinding.FragmentProfileBinding
import com.celia.catpedia_android.persistence.AppBreedsDataBase
import com.celia.catpedia_android.persistence.AppFavoritesDataBase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.profile_button.view.*


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.btNotification.tvBtnProfileButton.text = getString(R.string.off)
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.btEdit.tvBtnProfileButton.text = getString(R.string.dark_mode)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.btEdit.tvBtnProfileButton.text = getString(R.string.light_mode)
            }
        }
        val preferences = this.requireActivity().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val notificationEnabled: Boolean = preferences.getBoolean(getString(R.string.notification_enabled), false)
        setNotificationText(notificationEnabled)
        binding.tvEmail.text = preferences.getString(getString(R.string.email_string), getString(R.string.your_email))
        setButtonNotification()
        setDarkMode()
        deleteDataBase()
        selectLogOut()
        return binding.root
    }

    private fun setButtonNotification() {
        val preferences = this.requireActivity().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val preferencesToEdit = preferences.edit()
        binding.btNotification.tvBtnProfileButton.setOnClickListener {
            val notificationEnabled: Boolean = preferences.getBoolean(getString(R.string.notification_enabled), false)
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING)
            if (notificationEnabled) {
                preferencesToEdit.putBoolean(getString(R.string.notification_enabled), false)
                preferencesToEdit.apply()
                FirebaseMessaging.getInstance().unsubscribeFromTopic(getString(R.string.catpedia))
            }
            else {
                preferencesToEdit.putBoolean(getString(R.string.notification_enabled), true)
                preferencesToEdit.apply()
                FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.catpedia))
            }
            setNotificationText(!notificationEnabled)
        }
    }

    private fun setDarkMode() {
        //change darkMode to true
        val prefs = activity?.getSharedPreferences(getString(R.string.visual), Context.MODE_PRIVATE)?.edit()
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        binding.btEdit.tvBtnProfileButton.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING)
            if (AppCompatDelegate.MODE_NIGHT_YES == AppCompatDelegate.getDefaultNightMode() || nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                binding.btEdit.tvBtnProfileButton.text = getString(R.string.dark_mode)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                prefs?.putBoolean(getString(R.string.dark_visual), false)
            } else {
                binding.btEdit.tvBtnProfileButton.text = getString(R.string.light_mode)
                it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                prefs?.putBoolean(getString(R.string.dark_visual), true)
            }
            prefs?.apply()
        }
    }

    private fun deleteDataBase() {
        //clear all elements of the database
        binding.btFavorite.tvBtnProfileButton.setOnClickListener {
            //add haptic feedback
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING)
            val favoritesDataBase = AppFavoritesDataBase.getAppDatabase(requireContext()).favoritesDao()
            val database = AppBreedsDataBase.getAppDatabase(requireContext()).breedDao()
            val breeds = database.getAll()
            breeds.forEach { breed ->
                breed.favorite = false
            }
            favoritesDataBase.deleteAllFavorites()
        }
    }

    private fun selectLogOut() {
        binding.btLogOut.tvBtnProfileButton.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING)
            val preferences =
                activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)?.edit()
            preferences?.clear()
            preferences?.apply()
            val loginIntent = Intent(activity, LoginActivity::class.java)
            startActivity(loginIntent)
            FirebaseAuth.getInstance().signOut()
        }
    }

    private fun setNotificationText(notificationEnabled: Boolean) {
        if (notificationEnabled) {
            binding.btNotification.tvBtnProfileButton.text = getString(R.string.notification_on)
        }
        else {
            binding.btNotification.tvBtnProfileButton.text = getString(R.string.notification_off)
        }
    }
}