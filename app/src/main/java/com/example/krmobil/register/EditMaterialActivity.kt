package com.example.krmobil.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.krmobil.R
import com.example.krmobil.dbhelper.DBHelper
import com.example.krmobil.models.Material

class EditMaterialActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var categoryEditText: EditText
    private lateinit var updateMaterialButton: Button
    private lateinit var selectImageButton: Button

    private var imageResourceName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_material)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Включаем кнопку "Назад"
        supportActionBar?.title = ""

        imageView = findViewById(R.id.edit_material_image)
        nameEditText = findViewById(R.id.edit_material_name)
        descriptionEditText = findViewById(R.id.edit_material_description)
        priceEditText = findViewById(R.id.edit_material_price)
        categoryEditText = findViewById(R.id.edit_material_category)
        updateMaterialButton = findViewById(R.id.update_material_button)
        selectImageButton = findViewById(R.id.select_image_button)

        val materialId = intent.getIntExtra("materialId", -1)
        if (materialId == -1) {
            Toast.makeText(this, "Материал не найден", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val dbHelper = DBHelper(this, null)
        val material = dbHelper.getMaterialById(materialId)

        if (material != null) {
            imageResourceName = material.image
            imageView.setImageResource(getImageResourceId(material.image))
            nameEditText.setText(material.name)
            descriptionEditText.setText(material.description)
            priceEditText.setText(material.price.toString())
            categoryEditText.setText(material.category)
        }

        selectImageButton.setOnClickListener {
            selectImage()
        }

        updateMaterialButton.setOnClickListener {
            updateMaterial(materialId)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val userRole = getUserRole() // Метод для получения роли пользователя
        Log.d("EditMaterialActivity", "User role: $userRole")
        if (userRole == "admin") {
            menuInflater.inflate(R.menu.menu_admin, menu)
        } else if (userRole == "user") {
            menuInflater.inflate(R.menu.menu_user, menu)
        }
        return true
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            val uri: Uri? = data.data
            imageResourceName = getImageResourceName(uri)
            imageView.setImageURI(uri)
        }
    }

    private fun getImageResourceName(uri: Uri?): String? {
        // Здесь нужно реализовать логику для получения имени ресурса изображения
        // В данном примере просто возвращаем имя файла
        return uri?.lastPathSegment
    }

    private fun getImageResourceId(imageName: String): Int {
        return resources.getIdentifier(imageName, "drawable", packageName)
    }

    private fun updateMaterial(materialId: Int) {
        val name = nameEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val price = priceEditText.text.toString().toDoubleOrNull()
        val category = categoryEditText.text.toString()

        if (imageResourceName.isNullOrBlank() || name.isBlank() || description.isBlank() || price == null || category.isBlank()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        val dbHelper = DBHelper(this, null)
        val material = Material(
            id = materialId,
            image = imageResourceName!!,
            name = name,
            description = description,
            price = price!!,
            category = category
        )

        dbHelper.updateMaterial(material)
        Toast.makeText(this, "Материал обновлен!", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getUserRole(): String {
        // Реализуйте логику для получения роли пользователя
        // Например, из SharedPreferences или базы данных
        return "admin" // По умолчанию возвращаем "admin" для тестирования
    }
}
