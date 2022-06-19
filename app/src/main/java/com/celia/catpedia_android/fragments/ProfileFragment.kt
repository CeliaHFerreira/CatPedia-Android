package com.celia.catpedia_android.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.celia.catpedia_android.R
import com.celia.catpedia_android.databinding.FragmentProfileBinding
import com.celia.catpedia_android.persistence.AppFavoritesDataBase
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
        binding.tvEmail.text = preferences.getString("email", "email")
        setButtonNotification()
        setDarkMode()
        deleteDataBase()
        selectLogOut()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setButtonNotification() {
        binding.btNotification.tvBtnProfileButton.setOnClickListener {
            binding.btNotification.tvBtnProfileButton.text = getString(R.string.on)
            val notification = NotificationCompat.Builder(requireContext(), "chanel")
                .setSmallIcon(R.drawable.ic_favorite)
                .setContentTitle("Primera prueba")
                .setContentText("hola")
                .build()

            val manager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(1, notification)

            //create notification
            val notificationChannel = NotificationChannel("chanel", "chanel", NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(notificationChannel)

        }
    }

    fun setDarkMode() {
        //change darkMode to true
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        binding.btEdit.tvBtnProfileButton.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING)
            if (AppCompatDelegate.MODE_NIGHT_YES == AppCompatDelegate.getDefaultNightMode() || nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                binding.btEdit.tvBtnProfileButton.text = getString(R.string.dark_mode)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                binding.btEdit.tvBtnProfileButton.text = getString(R.string.light_mode)
                it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    fun deleteDataBase() {
        //clear all elements of the database
        binding.btFavorite.tvBtnProfileButton.setOnClickListener {
            //add haptic feedback
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING)
            val favoritesDataBase = AppFavoritesDataBase.getAppDatabase(requireContext()).favoritesDao()
            favoritesDataBase.deleteAllFavorites()
        }
    }

    fun selectLogOut() {
        binding.btLogOut.tvBtnProfileButton.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING)
            requireActivity().finish()
        }
    }
}