package com.celia.catpedia_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_enrollment.*

class EnrollmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enrollment)


        setup()
    }

    private fun setup() {
        enrollmentButton.setOnClickListener {
            if (emailET.text.isNotEmpty() && passwordET.text.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailET.text.toString(), passwordET.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful) {
                        showHome()
                    } else {
                        showAlert()
                    }
                }
            }
        }
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("se ha producido un error en el registro")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome() {
        val homeIntent = Intent(this, HomeActivity::class.java)
        startActivity(homeIntent)
    }

}