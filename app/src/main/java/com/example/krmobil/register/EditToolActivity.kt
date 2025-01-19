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

class EditToolActivity : AppCompatActivity() {
    private lateinit var imageEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var categoryEditText: EditText
    private lateinit var updateToolButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_tool)

        imageEditText = findViewById(R.id.edit_tool_image)
        nameEditText = findViewById(R.id.edit_tool_name)
        descriptionEditText = findViewById(R.id.edit_tool_description)
        priceEditText = findViewById(R.id.edit_tool_price)
        categoryEditText = findViewById(R.id.edit_tool_category)
        updateToolButton = findViewById(R.id.update_tool_button)

        val toolId = intent.getIntExtra("toolId", -1)
        if (toolId == -1) {
            Toast.makeText(this, "Инструмент не найден", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val dbHelper = DBHelper(this, null)
        val tool = dbHelper.getToolById(toolId)

        if (tool != null) {
            imageEditText.setText(tool.image)
            nameEditText.setText(tool.name)
            descriptionEditText.setText(tool.description)
            priceEditText.setText(tool.price.toString())
            categoryEditText.setText(tool.category)
        }

        updateToolButton.setOnClickListener {
            updateTool(toolId)
        }
    }

    private fun updateTool(toolId: Int) {
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
            id = toolId,
            image = image,
            name = name,
            description = description,
            price = price,
            category = category
        )

        dbHelper.updateTool(tool)
        Toast.makeText(this, "Инструмент обновлен!", Toast.LENGTH_SHORT).show()
        finish()
    }
}
