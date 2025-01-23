package com.example.krmobil.products

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.krmobil.R
import com.example.krmobil.dbhelper.DBHelper
import com.example.krmobil.register.AuthActivity
import com.example.krmobil.register.ProfileActivity
import com.example.krmobil.register.ShoppingCartActivity

class CatalogActivity : AppCompatActivity() {
    private var isAdmin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Включаем кнопку "Назад"
        supportActionBar?.title = ""

        val userEmail = intent.getStringExtra("useremail")
        if (userEmail != null) {
            val dbHelper = DBHelper(this, null)
            val user = dbHelper.getUserByEmail(userEmail)

            if (user != null) {
                isAdmin = user.isAdmin
                val buttonViewTools: Button = findViewById(R.id.button_view_tools)
                val buttonViewMaterials: Button = findViewById(R.id.button_view_materials)

                buttonViewTools.setOnClickListener {
                    val intent = Intent(this, ToolListActivity::class.java)
                    intent.putExtra("isAdmin", user.isAdmin)
                    startActivity(intent)
                }

                buttonViewMaterials.setOnClickListener {
                    val intent = Intent(this, MaterialListActivity::class.java)
                    intent.putExtra("isAdmin", user.isAdmin)
                    startActivity(intent)
                }
            } else {
                Log.e("CatalogActivity", "User not found for email: $userEmail")
                // Перенаправление на страницу авторизации или другую обработку ошибки
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
                finish()
            }
        } else {
            Log.e("CatalogActivity", "User email is null")
            // Перенаправление на страницу авторизации или другую обработку ошибки
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (isAdmin) {
            menu?.findItem(R.id.action_cart)?.isVisible = false
        } else {
            menu?.findItem(R.id.action_cart)?.isVisible = true
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_cart -> {
                val intent = Intent(this, ShoppingCartActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                return true
            }
            android.R.id.home -> { // Обработка нажатия на кнопку "Назад"
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
