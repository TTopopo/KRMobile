package com.example.krmobil.register

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.krmobil.R
import com.example.krmobil.dbhelper.DBHelper
import com.example.krmobil.models.Comment

class ReviewsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var reviewsAdapter: ReviewsAdapter
    private lateinit var dbHelper: DBHelper
    private var itemId: Int = 0
    private var itemType: String = ""
    private lateinit var noCommentsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Включаем кнопку "Назад"
        supportActionBar?.title = "Отзывы"

        itemId = intent.getIntExtra("itemId", 0)
        itemType = intent.getStringExtra("itemType") ?: ""

        recyclerView = findViewById(R.id.recyclerViewReviews)
        noCommentsTextView = findViewById(R.id.no_comments_text)
        dbHelper = DBHelper(this, null)

        val comments = dbHelper.getCommentsByItemIdAndType(itemId, itemType)
        if (comments.isEmpty()) {
            noCommentsTextView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            noCommentsTextView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            reviewsAdapter = ReviewsAdapter(comments, this)
            recyclerView.adapter = reviewsAdapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_user, menu)
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

    override fun onResume() {
        super.onResume()
        // Обновляем данные при каждом входе в активность
        val comments = dbHelper.getCommentsByItemIdAndType(itemId, itemType)
        if (comments.isEmpty()) {
            noCommentsTextView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            noCommentsTextView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            reviewsAdapter.updateComments(comments)
        }
    }
}
