package com.example.krmobil.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.krmobil.R
import com.example.krmobil.dbhelper.DBHelper
import com.example.krmobil.products.CatalogActivity
import com.example.krmobil.utils.SharedPreferencesHelper

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        val userEmail: EditText = findViewById(R.id.user_email_auth)
        val userPassword: EditText = findViewById(R.id.user_pass_auth)
        val button: Button = findViewById(R.id.button_auth)
        val linkToReg: TextView = findViewById(R.id.link_to_reg)

        button.setOnClickListener {
            val email = userEmail.text.toString().trim()
            val pass = userPassword.text.toString().trim()

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            } else {
                val db = DBHelper(this, null)
                val isAuth = db.getUser(email, pass)
                if (isAuth) {
                    Toast.makeText(this, "Пользователь $email авторизован", Toast.LENGTH_LONG).show()
                    userEmail.text.clear()
                    userPassword.text.clear()

                    // Получение логина пользователя из базы данных
                    val user = db.getUserByEmail(email)
                    val userLogin = user?.login ?: "default_login"

                    // Сохранение email и логина в SharedPreferences
                    SharedPreferencesHelper.saveUserEmail(this, email)
                    SharedPreferencesHelper.saveUserLogin(this, userLogin)

                    // Переход на активность с выбором каталога
                    val intent = Intent(this, CatalogActivity::class.java)
                    intent.putExtra("useremail", email)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Пользователь $email не авторизован. Проверьте заполнение полей", Toast.LENGTH_LONG).show()
                }
            }
        }

        linkToReg.setOnClickListener {
            val intent = Intent(this, RegActivity::class.java)
            startActivity(intent)
        }
    }
}
