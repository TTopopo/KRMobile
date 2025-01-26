package com.example.krmobil.products

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.krmobil.R
import com.example.krmobil.dbhelper.DBHelper
import com.example.krmobil.models.Tool
import com.example.krmobil.register.AddToolActivity
import com.example.krmobil.register.ProfileActivity
import com.example.krmobil.register.ReviewsActivity
import com.example.krmobil.register.ShoppingCartActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ToolListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var toolAdapter: ToolListAdapter
    private lateinit var dbHelper: DBHelper
    private lateinit var searchView: SearchView
    private lateinit var toolbar: Toolbar
    private var isAdmin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tool_list)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Включаем кнопку "Назад"
        supportActionBar?.title = ""

        recyclerView = findViewById(R.id.recyclerViewTools)
        searchView = findViewById(R.id.searchView)
        dbHelper = DBHelper(this, null)

        isAdmin = intent.getBooleanExtra("isAdmin", false)
        val tools = dbHelper.getTools()
        Log.d("ToolListActivity", "Tools: $tools")
        toolAdapter = ToolListAdapter(tools, this, isAdmin)
        recyclerView.adapter = toolAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val addToolButton: Button = findViewById(R.id.button_add_tool)
        if (isAdmin) {
            addToolButton.visibility = View.VISIBLE
            addToolButton.setOnClickListener {
                val intent = Intent(this, AddToolActivity::class.java)
                startActivity(intent)
            }
        } else {
            addToolButton.visibility = View.GONE
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterTools(newText)
                return true
            }
        })
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

    private fun filterTools(query: String?) {
        val filteredTools = if (query.isNullOrEmpty()) {
            dbHelper.getTools()
        } else {
            dbHelper.getTools().filter { tool ->
                tool.name.contains(query, ignoreCase = true) ||
                        tool.description.contains(query, ignoreCase = true) ||
                        tool.category.contains(query, ignoreCase = true)
            }
        }
        Log.d("ToolListActivity", "Filtered Tools: $filteredTools")
        toolAdapter.updateTools(filteredTools)
    }
}
