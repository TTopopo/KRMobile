package com.example.krmobil.register

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.krmobil.R
import com.example.krmobil.products.AdminSaleAdapter
import com.example.krmobil.dbhelper.DBHelper

class AdminSaleActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adminSaleAdapter: AdminSaleAdapter
    private lateinit var dbHelper: DBHelper
    private var isAdmin: Boolean = true // По умолчанию админ

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_sale)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Включаем кнопку "Назад"
        supportActionBar?.title = ""

        recyclerView = findViewById(R.id.recyclerViewPurchaseHistory)
        dbHelper = DBHelper(this, null)

        val sales = dbHelper.getSales()
        adminSaleAdapter = AdminSaleAdapter(sales)
        recyclerView.adapter = adminSaleAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isAdmin) {
            menuInflater.inflate(R.menu.menu_admin, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
