package com.celia.catpedia_android.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.celia.catpedia_android.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.profile_button.view.*

class LoginActivity : AppCompatActivity() {

    private val GOOGLE_SIGN_IN = 200

    companion object {
        private const val SMS_PERMISSION_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Firebase analytics
        var firebaseAnalytics = Firebase.analytics
        val bundle = Bundle()
        bundle.putString("message", "App iniciada")
        firebaseAnalytics.logEvent("InitScreen", bundle)

        //Home setup
        setup()

        session()
    }

    override fun onStart() {
        super.onStart()
        loginLayout.visibility = View.VISIBLE
        setMode()
    }

    private fun session() {
        val preferences = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = preferences.getString("email", null)

        if (email != null) {
            loginLayout.visibility = View.INVISIBLE
            showHome(email)
        }
    }

    private fun setup() {
        signInButton.setOnClickListener {
            showEnrollment()
            checkPermission(android.Manifest.permission.READ_SMS , SMS_PERMISSION_CODE)
        }
        loginButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            showHome(it.result?.user?.email ?: "")
                        } else {
                            showAlert()
                        }
                    }
            }
        }
        googleSingIn.setOnClickListener {
            /*val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)*/
        }
    }

    private fun showEnrollment() {
        val enrollmentIntent = Intent(this, EnrollmentActivity::class.java)
        startActivity(enrollmentIntent)
    }

    private fun showAlert() {
        Toast.makeText(baseContext, R.string.login_error, Toast.LENGTH_LONG).show()
    }

    private fun showHome(email: String) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
        }
        startActivity(homeIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                showHome(account.email ?: "")
                            } else {
                                showAlert()
                            }
                        }

                }
            } catch (err: ApiException) {
                showAlert()
            }
        }
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this@LoginActivity, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(this@LoginActivity, arrayOf(permission), requestCode)
        } else {
            Toast.makeText(this@LoginActivity, "Permission already granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@LoginActivity, "Sms Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@LoginActivity, "Sms Permission Denied", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@LoginActivity, "Sms Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@LoginActivity, "Sms Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setMode() {
        val prefs = getSharedPreferences("visual", Context.MODE_PRIVATE)
        val prefsToEdit = prefs?.edit()
        val darkmode = prefs.getBoolean("darkmode", false)
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (darkmode || nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            prefsToEdit?.putBoolean("darkmode", true)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            prefsToEdit?.putBoolean("darkmode", false)
        }
        prefsToEdit?.apply()
    }
}