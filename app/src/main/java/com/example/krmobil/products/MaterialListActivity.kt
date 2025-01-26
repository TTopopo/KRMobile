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
import com.example.krmobil.models.Material
import com.example.krmobil.register.AddMaterialActivity
import com.example.krmobil.register.ProfileActivity
import com.example.krmobil.register.ShoppingCartActivity

class MaterialListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var materialAdapter: MaterialListAdapter
    private lateinit var dbHelper: DBHelper
    private lateinit var searchView: SearchView
    private lateinit var toolbar: Toolbar
    private var isAdmin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_list)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Включаем кнопку "Назад"
        supportActionBar?.title = ""

        recyclerView = findViewById(R.id.recyclerViewMaterials)
        searchView = findViewById(R.id.searchView)
        dbHelper = DBHelper(this, null)

        isAdmin = intent.getBooleanExtra("isAdmin", false)
        val materials = dbHelper.getMaterials()
        materialAdapter = MaterialListAdapter(materials, this, isAdmin)
        recyclerView.adapter = materialAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val addMaterialButton: Button = findViewById(R.id.button_add_material)
        if (isAdmin) {
            addMaterialButton.visibility = View.VISIBLE
            addMaterialButton.setOnClickListener {
                val intent = Intent(this, AddMaterialActivity::class.java)
                startActivity(intent)
            }
        } else {
            addMaterialButton.visibility = View.GONE
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterMaterials(newText)
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

    private fun filterMaterials(query: String?) {
        val filteredMaterials = if (query.isNullOrEmpty()) {
            dbHelper.getMaterials()
        } else {
            dbHelper.getMaterials().filter { material ->
                material.name.contains(query, ignoreCase = true) ||
                        material.description.contains(query, ignoreCase = true) ||
                        material.category.contains(query, ignoreCase = true)
            }
        }
        Log.d("MaterialListActivity", "Filtered Materials: $filteredMaterials")
        materialAdapter.updateMaterials(filteredMaterials)
    }
}
