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

class EditToolActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var categoryEditText: EditText
    private lateinit var updateToolButton: Button
    private lateinit var selectImageButton: Button

    private var imageResourceName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_tool)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Включаем кнопку "Назад"
        supportActionBar?.title = ""

        imageView = findViewById(R.id.edit_tool_image)
        nameEditText = findViewById(R.id.edit_tool_name)
        descriptionEditText = findViewById(R.id.edit_tool_description)
        priceEditText = findViewById(R.id.edit_tool_price)
        categoryEditText = findViewById(R.id.edit_tool_category)
        updateToolButton = findViewById(R.id.update_tool_button)
        selectImageButton = findViewById(R.id.select_image_button)

        val toolId = intent.getIntExtra("toolId", -1)
        if (toolId == -1) {
            Toast.makeText(this, "Инструмент не найден", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val dbHelper = DBHelper(this, null)
        val tool = dbHelper.getToolById(toolId)

        if (tool != null) {
            imageResourceName = tool.image
            imageView.setImageResource(getImageResourceId(tool.image))
            nameEditText.setText(tool.name)
            descriptionEditText.setText(tool.description)
            priceEditText.setText(tool.price.toString())
            categoryEditText.setText(tool.category)
        }

        selectImageButton.setOnClickListener {
            selectImage()
        }

        updateToolButton.setOnClickListener {
            updateTool(toolId)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val userRole = getUserRole() // Метод для получения роли пользователя
        Log.d("EditToolActivity", "User role: $userRole")
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

    private fun updateTool(toolId: Int) {
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
            id = toolId,
            image = imageResourceName!!,
            name = name,
            description = description,
            price = price!!,
            category = category
        )

        dbHelper.updateTool(tool)
        Toast.makeText(this, "Инструмент обновлен!", Toast.LENGTH_SHORT).show()
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
