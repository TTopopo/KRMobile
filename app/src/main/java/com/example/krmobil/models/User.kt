package com.example.krmobil.models

data class User(
    val email: String,
    val login: String,
    val phone: String,
    val pass: String,
    val isAdmin: Boolean
)