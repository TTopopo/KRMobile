package com.example.krmobil.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.krmobil.R
import com.example.krmobil.dbhelper.DBHelper
import com.example.krmobil.models.Material

class EditMaterialActivity : AppCompatActivity() {
    private lateinit var imageEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var categoryEditText: EditText
    private lateinit var updateMaterialButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_material)

        imageEditText = findViewById(R.id.edit_material_image)
        nameEditText = findViewById(R.id.edit_material_name)
        descriptionEditText = findViewById(R.id.edit_material_description)
        priceEditText = findViewById(R.id.edit_material_price)
        categoryEditText = findViewById(R.id.edit_material_category)
        updateMaterialButton = findViewById(R.id.update_material_button)

        val materialId = intent.getIntExtra("materialId", -1)
        if (materialId == -1) {
            Toast.makeText(this, "Материал не найден", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val dbHelper = DBHelper(this, null)
        val material = dbHelper.getMaterialById(materialId)

        if (material != null) {
            imageEditText.setText(material.image)
            nameEditText.setText(material.name)
            descriptionEditText.setText(material.description)
            priceEditText.setText(material.price.toString())
            categoryEditText.setText(material.category)
        }

        updateMaterialButton.setOnClickListener {
            updateMaterial(materialId)
        }
    }

    private fun updateMaterial(materialId: Int) {
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
        val material = Material(
            id = materialId,
            image = image,
            name = name,
            description = description,
            price = price,
            category = category
        )

        dbHelper.updateMaterial(material)
        Toast.makeText(this, "Материал обновлен!", Toast.LENGTH_SHORT).show()
        finish()
    }
}
