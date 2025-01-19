package com.example.krmobil.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.krmobil.R
import com.example.krmobil.dbhelper.DBHelper
import com.example.krmobil.models.Tool

class AddToolActivity : AppCompatActivity() {
    private lateinit var imageEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var categoryEditText: EditText
    private lateinit var addToolButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tool)

        imageEditText = findViewById(R.id.add_tool_image)
        nameEditText = findViewById(R.id.add_tool_name)
        descriptionEditText = findViewById(R.id.add_tool_description)
        priceEditText = findViewById(R.id.add_tool_price)
        categoryEditText = findViewById(R.id.add_tool_category)
        addToolButton = findViewById(R.id.add_tool_button)

        addToolButton.setOnClickListener {
            addTool()
        }
    }

    private fun addTool() {
        val image = imageEditText.text.toString()
        val name = nameEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val price = priceEditText.text.toString().toDoubleOrNull()
        val category = categoryEditText.text.toString()

        if (image.isBlank() || name.isBlank() || description.isBlank() || price == null || category.isBlank()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        val dbHelper = DBHelper(this, null)
        val tool = Tool(
            id = 0,
            image = image,
            name = name,
            description = description,
            price = price!!,
            category = category
        )

        dbHelper.addTool(tool)
        Toast.makeText(this, "Инструмент добавлен!", Toast.LENGTH_SHORT).show()
        finish()
    }
}
