package com.example.krmobil.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.krmobil.R
import com.example.krmobil.dbhelper.DBHelper
import com.example.krmobil.products.AdminUserAdapter
import com.example.krmobil.register.ShoppingCartActivity
import com.example.krmobil.utils.SharedPreferencesHelper

class ProfileActivity : AppCompatActivity() {
    private var isAdmin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Включаем кнопку "Назад"
        supportActionBar?.title = ""

        val emailTextView: TextView = findViewById(R.id.profile_email)
        val loginTextView: TextView = findViewById(R.id.profile_login)
        val phoneTextView: TextView = findViewById(R.id.profile_phone)
        val adminPanel: Button = findViewById(R.id.profile_admin)
        val history: Button = findViewById(R.id.profile_history)
        val recyclerView: RecyclerView = findViewById(R.id.profile_items_list)

        val userEmail = intent.getStringExtra("useremail") ?: SharedPreferencesHelper.getUserEmail(this)
        if (userEmail == null) {
            Toast.makeText(this, "Ошибка: email не найден", Toast.LENGTH_LONG).show()
            return
        }

        val dbHelper = DBHelper(this, null)
        val user = dbHelper.getUserByEmail(userEmail)

        if (user != null) {
            isAdmin = user.isAdmin
            emailTextView.text = user.email
            loginTextView.text = user.login
            phoneTextView.text = user.phone

            Log.d("ProfileActivity", "User role: ${if (isAdmin) "Admin" else "User"}")

            if (isAdmin) {
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

        // Установка адаптера для RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = AdminUserAdapter(emptyList()) // Используйте ваш адаптер
        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isAdmin) {
            menuInflater.inflate(R.menu.menu_admin, menu)
        } else {
            menuInflater.inflate(R.menu.menu_user, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_cart -> {
                val intent = Intent(this, ShoppingCartActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_profile -> {
                // Здесь можно добавить логику для обработки нажатия на профиль, если необходимо
                return true
            }
            android.R.id.home -> { // Обработка нажатия на кнопку "Назад"
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
