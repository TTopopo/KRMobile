// PurchaseHistoryActivity.kt
package com.example.krmobil.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.krmobil.R
import com.example.krmobil.dbhelper.DBHelper
import com.example.krmobil.products.PurchaseHistoryAdapter

class PurchaseHistoryActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var purchaseHistoryAdapter: PurchaseHistoryAdapter
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_history)

        recyclerView = findViewById(R.id.recyclerViewPurchaseHistory)
        dbHelper = DBHelper(this, null)

        val sales = dbHelper.getSales()
        purchaseHistoryAdapter = PurchaseHistoryAdapter(sales)
        recyclerView.adapter = purchaseHistoryAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
