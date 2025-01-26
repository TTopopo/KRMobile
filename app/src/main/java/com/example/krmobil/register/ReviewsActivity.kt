package com.example.krmobil.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.krmobil.R
import com.example.krmobil.dbhelper.DBHelper
import com.example.krmobil.models.Comment

class ReviewsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var reviewsAdapter: ReviewsAdapter
    private lateinit var dbHelper: DBHelper
    private var saleId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews)

        saleId = intent.getIntExtra("saleId", 0)

        recyclerView = findViewById(R.id.recyclerViewReviews)
        dbHelper = DBHelper(this, null)

        val comments = dbHelper.getCommentsBySaleId(saleId)
        reviewsAdapter = ReviewsAdapter(comments, this)
        recyclerView.adapter = reviewsAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        // Обновляем данные при каждом входе в активность
        val comments = dbHelper.getCommentsBySaleId(saleId)
        reviewsAdapter.updateComments(comments)
    }
}
