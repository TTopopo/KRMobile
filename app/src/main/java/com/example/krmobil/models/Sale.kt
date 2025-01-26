package com.example.krmobil.models

data class Sale(
    val id: Int,
    val itemId: Int,
    val itemType: String,
    val itemName: String,
    val itemImage: String,
    val itemDescription: String,
    val price: Double,
    val quantity: Int,
    val saleDate: String
)
