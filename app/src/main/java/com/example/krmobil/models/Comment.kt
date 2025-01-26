package com.example.krmobil.models

data class Comment(
    val id: Int,
    val saleId: Int,
    val userLogin: String, // Добавлено поле userLogin
    val comment: String
)
