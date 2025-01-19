// AdminUserActivity.kt
package com.example.krmobil.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.krmobil.R
import com.example.krmobil.products.AdminUserAdapter
import com.example.krmobil.dbhelper.DBHelper

class AdminUserActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adminUserAdapter: AdminUserAdapter
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_user)

        recyclerView = findViewById(R.id.recyclerViewUsers)
        dbHelper = DBHelper(this, null)

        val users = dbHelper.getAllUsers()
        adminUserAdapter = AdminUserAdapter(users)
        recyclerView.adapter = adminUserAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
