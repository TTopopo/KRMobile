package com.example.krmobil.register

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.krmobil.R
import com.example.krmobil.models.Comment

class ReviewsAdapter(private var comments: List<Comment>, private val context: Context) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.review_item_username)
        val commentText: TextView = view.findViewById(R.id.review_item_comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val comment = comments[position]
        holder.username.text = comment.userLogin // Отображаем логин пользователя
        holder.commentText.text = comment.comment
    }

    fun updateComments(newComments: List<Comment>) {
        comments = newComments
        notifyDataSetChanged()
    }
}

