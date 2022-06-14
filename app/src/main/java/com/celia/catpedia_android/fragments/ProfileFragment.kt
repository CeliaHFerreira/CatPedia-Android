package com.celia.catpedia_android.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
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
        binding.btNotification.text = "OFF"
        setButtonNotification()
        setDarkMode()
        deleteDataBase()
        selectLogOut()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setButtonNotification() {
        binding.btNotification.setOnClickListener {
            binding.btNotification.text = "ON"
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

    private fun setDarkMode() {
        //change darkMode to true
        binding.btEdit.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING)
            if (AppCompatDelegate.MODE_NIGHT_YES == AppCompatDelegate.getDefaultNightMode()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    private fun deleteDataBase() {
        //clear all elements of the database
        binding.btFavorite.setOnClickListener {
            //add haptic feedback
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING)
            val favoritesDataBase = AppFavoritesDataBase.getAppDatabase(requireContext()).favoritesDao()
            favoritesDataBase.deleteAllFavorites()
        }
    }

    private fun selectLogOut() {
        binding.btLogOut.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING)
            requireActivity().finish()
        }
    }
}