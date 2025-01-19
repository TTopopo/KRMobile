// ProfileActivity.kt
package com.example.krmobil.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.krmobil.R
import com.example.krmobil.dbhelper.DBHelper
import com.example.krmobil.utils.SharedPreferencesHelper

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val emailTextView: TextView = findViewById(R.id.profile_email)
        val loginTextView: TextView = findViewById(R.id.profile_login)
        val phoneTextView: TextView = findViewById(R.id.profile_phone)
        val adminPanel: Button = findViewById(R.id.profile_admin)
        val history: Button = findViewById(R.id.profile_history)

        val userEmail = intent.getStringExtra("useremail") ?: SharedPreferencesHelper.getUserEmail(this)
        if (userEmail == null) {
            Toast.makeText(this, "Ошибка: email не найден", Toast.LENGTH_LONG).show()
            return
        }

        val dbHelper = DBHelper(this, null)
        val user = dbHelper.getUserByEmail(userEmail)

        if (user != null) {
            emailTextView.text = user.email
            loginTextView.text = user.login
            phoneTextView.text = user.phone

            if (user.isAdmin) {
                adminPanel.visibility = View.VISIBLE
                history.visibility = View.GONE
            } else {
                adminPanel.visibility = View.GONE
                history.visibility = View.VISIBLE
            }
        } else {
            Toast.makeText(this, "Пользователь не найден", Toast.LENGTH_LONG).show()
        }

        history.setOnClickListener {
            val intent = Intent(this, PurchaseHistoryActivity::class.java)
            intent.putExtra("useremail", userEmail)
            startActivity(intent)
        }

        adminPanel.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            intent.putExtra("useremail", userEmail)
            startActivity(intent)
        }
    }
}
