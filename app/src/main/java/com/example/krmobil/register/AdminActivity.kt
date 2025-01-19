// AdminActivity.kt
package com.example.krmobil.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.krmobil.R

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val salesPanel: Button = findViewById(R.id.admin_sales)
        val userPanel: Button = findViewById(R.id.admin_users)

        salesPanel.setOnClickListener {
            val intent = Intent(this, AdminSaleActivity::class.java)
            startActivity(intent)
        }

        userPanel.setOnClickListener {
            val intent = Intent(this, AdminUserActivity::class.java)
            startActivity(intent)
        }
    }
}
