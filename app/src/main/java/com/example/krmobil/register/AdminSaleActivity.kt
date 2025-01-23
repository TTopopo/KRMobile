package com.example.krmobil.register

import android.os.Bundle
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
