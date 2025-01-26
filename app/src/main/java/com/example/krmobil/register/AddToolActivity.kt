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
import com.example.krmobil.models.Tool

class AddToolActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var categoryEditText: EditText
    private lateinit var addToolButton: Button
    private lateinit var selectImageButton: Button

    private var imageResourceName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tool)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Включаем кнопку "Назад"
        supportActionBar?.title = ""

        imageView = findViewById(R.id.add_tool_image)
        nameEditText = findViewById(R.id.add_tool_name)
        descriptionEditText = findViewById(R.id.add_tool_description)
        priceEditText = findViewById(R.id.add_tool_price)
        categoryEditText = findViewById(R.id.add_tool_category)
        addToolButton = findViewById(R.id.add_tool_button)
        selectImageButton = findViewById(R.id.select_image_button)

        selectImageButton.setOnClickListener {
            selectImage()
        }

        addToolButton.setOnClickListener {
            addTool()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val userRole = getUserRole() // Метод для получения роли пользователя
        Log.d("AddToolActivity", "User role: $userRole")
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

    private fun addTool() {
        val name = nameEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val price = priceEditText.text.toString().toDoubleOrNull()
        val category = categoryEditText.text.toString()

        if (imageResourceName.isNullOrBlank() || name.isBlank() || description.isBlank() || price == null || category.isBlank()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        val dbHelper = DBHelper(this, null)
        val tool = Tool(
            id = 0,
            image = imageResourceName!!,
            name = name,
            description = description,
            price = price!!,
            category = category
        )

        dbHelper.addTool(tool)
        Toast.makeText(this, "Инструмент добавлен!", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getUserRole(): String {
        // Реализуйте логику для получения роли пользователя
        // Например, из SharedPreferences или базы данных
        // Для примера возвращаем "admin"
        return "admin" // По умолчанию возвращаем "admin" для тестирования
    }
}
