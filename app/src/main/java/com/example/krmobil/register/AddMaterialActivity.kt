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

class AddMaterialActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var categoryEditText: EditText
    private lateinit var addMaterialButton: Button
    private lateinit var selectImageButton: Button

    private var imageResourceName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_material)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Включаем кнопку "Назад"
        supportActionBar?.title = ""

        imageView = findViewById(R.id.add_material_image)
        nameEditText = findViewById(R.id.add_material_name)
        descriptionEditText = findViewById(R.id.add_material_description)
        priceEditText = findViewById(R.id.add_material_price)
        categoryEditText = findViewById(R.id.add_material_category)
        addMaterialButton = findViewById(R.id.add_material_button)
        selectImageButton = findViewById(R.id.select_image_button)

        selectImageButton.setOnClickListener {
            selectImage()
        }

        addMaterialButton.setOnClickListener {
            addMaterial()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val userRole = getUserRole() // Метод для получения роли пользователя
        Log.d("AddMaterialActivity", "User role: $userRole")
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

    private fun addMaterial() {
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
            id = 0,
            image = imageResourceName!!,
            name = name,
            description = description,
            price = price!!,
            category = category
        )

        dbHelper.addMaterial(material)
        Toast.makeText(this, "Материал добавлен!", Toast.LENGTH_SHORT).show()
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
