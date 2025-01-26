package com.example.krmobil.dbhelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.krmobil.models.Tool
import com.example.krmobil.models.Material
import com.example.krmobil.models.Sale
import com.example.krmobil.models.User
import com.example.krmobil.models.Comment
import com.example.krmobil.utils.CryptoUtils
import javax.crypto.SecretKey

class DBHelper(val context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "app", factory, 21) {

    private val secretKey: SecretKey = CryptoUtils.getKey(context)

    override fun onCreate(db: SQLiteDatabase?) {
        val queryTools = "CREATE TABLE tools (id INTEGER PRIMARY KEY AUTOINCREMENT, image TEXT, name TEXT, description TEXT, price REAL, category TEXT)"
        val queryMaterials = "CREATE TABLE materials (id INTEGER PRIMARY KEY AUTOINCREMENT, image TEXT, name TEXT, description TEXT, price REAL, category TEXT)"
        val queryUsers = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, login TEXT, phone TEXT, pass TEXT, is_admin INTEGER)"
        val querySales = "CREATE TABLE sales (id INTEGER PRIMARY KEY AUTOINCREMENT, item_id INTEGER, item_type TEXT, item_name TEXT, item_image TEXT, item_description TEXT, price REAL, quantity INTEGER, sale_date TEXT, FOREIGN KEY(item_id) REFERENCES tools(id) ON DELETE CASCADE, FOREIGN KEY(item_id) REFERENCES materials(id) ON DELETE CASCADE)"
        val queryComments = "CREATE TABLE comments (id INTEGER PRIMARY KEY AUTOINCREMENT, sale_id INTEGER, comment TEXT, FOREIGN KEY(sale_id) REFERENCES sales(id) ON DELETE CASCADE)"
        db!!.execSQL(queryTools)
        db.execSQL(queryMaterials)
        db.execSQL(queryUsers)
        db.execSQL(querySales)
        db.execSQL(queryComments)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 20) {
            updateCommentsTable(db!!)
        }
        // Удаляем таблицы только при значительных изменениях схемы
        if (oldVersion < 21) {
            db!!.execSQL("DROP TABLE IF EXISTS tools")
            db.execSQL("DROP TABLE IF EXISTS materials")
            db.execSQL("DROP TABLE IF EXISTS users")
            db.execSQL("DROP TABLE IF EXISTS sales")
            db.execSQL("DROP TABLE IF EXISTS comments")
            onCreate(db)
        }
    }

    private fun updateCommentsTable(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE comments_new (id INTEGER PRIMARY KEY AUTOINCREMENT, sale_id INTEGER, comment TEXT, FOREIGN KEY(sale_id) REFERENCES sales(id) ON DELETE CASCADE)")
        db.execSQL("INSERT INTO comments_new (sale_id, comment) SELECT sale_id, comment FROM comments")
        db.execSQL("DROP TABLE comments")
        db.execSQL("ALTER TABLE comments_new RENAME TO comments")
    }

    fun addTool(tool: Tool) {
        val values = ContentValues()
        values.put("image", tool.image)
        values.put("name", tool.name)
        values.put("description", tool.description)
        values.put("price", tool.price)
        values.put("category", tool.category)

        val db = this.writableDatabase
        db.insert("tools", null, values)
        db.close()
    }

    fun getTools(): List<Tool> {
        val tools = mutableListOf<Tool>()
        val db = this.readableDatabase
        var cursor = db.rawQuery("SELECT * FROM tools", null)

        if (cursor.count == 0) {
            addTool(Tool(1, "hammer", "Молоток", "Обычный молоток для общего использования", 150.99, "Ручные инструменты"))
            addTool(Tool(2, "otvertka", "Отвертка", "Обычная отвертка для общего использования", 49.99, "Ручные инструменты"))
            addTool(Tool(3, "drel", "Дрель", "Мощная дрель для сверления отверстий", 4990.99, "Электроинструменты"))
            addTool(Tool(4, "pila", "Пила", "Пила для распиловки древесины", 1200.90, "Ручные инструменты"))
            addTool(Tool(5, "gaichniu_cluch", "Гаечный ключ", "Гаечный ключ для затяжки болтов", 999.99, "Ручные инструменты"))

            cursor.close()
            val newCursor = db.rawQuery("SELECT * FROM tools", null)
            cursor = newCursor
        }

        if (cursor.moveToFirst()) {
            do {
                val tool = Tool(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    image = cursor.getString(cursor.getColumnIndexOrThrow("image")),
                    name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    price = cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                    category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
                )
                tools.add(tool)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return tools
    }

    fun getToolById(toolId: Int): Tool? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tools WHERE id = $toolId", null)
        var tool: Tool? = null
        if (cursor.moveToFirst()) {
            tool = Tool(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                image = cursor.getString(cursor.getColumnIndexOrThrow("image")),
                name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                price = cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
            )
        }
        cursor.close()
        db.close()
        return tool
    }

    fun updateTool(tool: Tool) {
        val values = ContentValues()
        values.put("image", tool.image)
        values.put("name", tool.name)
        values.put("description", tool.description)
        values.put("price", tool.price)
        values.put("category", tool.category)

        val db = this.writableDatabase
        db.update("tools", values, "id = ?", arrayOf(tool.id.toString()))
        db.close()
    }

    fun deleteTool(toolId: Int) {
        val db = this.writableDatabase
        db.delete("tools", "id = ?", arrayOf(toolId.toString()))
        db.close()
    }

    fun addMaterial(material: Material) {
        val values = ContentValues()
        values.put("image", material.image)
        values.put("name", material.name)
        values.put("description", material.description)
        values.put("price", material.price)
        values.put("category", material.category)

        val db = this.writableDatabase
        db.insert("materials", null, values)
        db.close()
    }

    fun getMaterials(): List<Material> {
        val materials = mutableListOf<Material>()
        val db = this.readableDatabase
        var cursor = db.rawQuery("SELECT * FROM materials", null)

        if (cursor.count == 0) {
            addMaterial(Material(1, "gvozdi", "Гвозди", "Гвозди для крепления", 1.0, "Крепежные материалы"))
            addMaterial(Material(2, "vinti", "Винты M6", "Винты для крепления", 2.0, "Крепежные материалы"))
            addMaterial(Material(3, "bolti", "Болты M6", "Болты для крепления", 3.0, "Крепежные материалы"))
            addMaterial(Material(4, "shaibi", "Шайбы M6", "Шайбы для крепления", 4.0, "Крепежные материалы"))
            addMaterial(Material(5, "gaiki", "Гайки M6", "Гайки для крепления", 5.0, "Крепежные материалы"))

            cursor.close()
            val newCursor = db.rawQuery("SELECT * FROM materials", null)
            cursor = newCursor
        }

        if (cursor.moveToFirst()) {
            do {
                val material = Material(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    image = cursor.getString(cursor.getColumnIndexOrThrow("image")),
                    name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    price = cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                    category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
                )
                materials.add(material)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return materials
    }

    fun getMaterialById(materialId: Int): Material? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM materials WHERE id = $materialId", null)
        var material: Material? = null
        if (cursor.moveToFirst()) {
            material = Material(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                image = cursor.getString(cursor.getColumnIndexOrThrow("image")),
                name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                price = cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
            )
        }
        cursor.close()
        db.close()
        return material
    }

    fun updateMaterial(material: Material) {
        val values = ContentValues()
        values.put("image", material.image)
        values.put("name", material.name)
        values.put("description", material.description)
        values.put("price", material.price)
        values.put("category", material.category)

        val db = this.writableDatabase
        db.update("materials", values, "id = ?", arrayOf(material.id.toString()))
        db.close()
    }

    fun deleteMaterial(materialId: Int) {
        val db = this.writableDatabase
        db.delete("materials", "id = ?", arrayOf(materialId.toString()))
        db.close()
    }

    fun addUser(user: User) {
        val values = ContentValues()
        values.put("email", user.email)
        values.put("login", user.login)
        values.put("phone", user.phone)
        values.put("pass", CryptoUtils.encrypt(user.pass, secretKey))
        values.put("is_admin", if (user.isAdmin) 1 else 0)

        val db = this.writableDatabase
        db.insert("users", null, values)
        db.close()
    }

    fun getUser(email: String, pass: String): Boolean {
        val db = this.readableDatabase
        val encryptedPass = CryptoUtils.encrypt(pass, secretKey)
        val result = db.rawQuery("SELECT * FROM users WHERE email = '$email' AND pass = '$encryptedPass'", null)
        val exists = result.moveToFirst()
        result.close()
        db.close()
        return exists
    }

    fun getUserById(userId: Int): User? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE id = $userId", null)
        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                login = cursor.getString(cursor.getColumnIndexOrThrow("login")),
                phone = cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                pass = CryptoUtils.decrypt(cursor.getString(cursor.getColumnIndexOrThrow("pass")), secretKey),
                isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow("is_admin")) == 1
            )
        }
        cursor.close()
        db.close()
        return user
    }

    fun getUserByEmail(email: String): User? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email = '$email'", null)
        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                login = cursor.getString(cursor.getColumnIndexOrThrow("login")),
                phone = cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                pass = CryptoUtils.decrypt(cursor.getString(cursor.getColumnIndexOrThrow("pass")), secretKey),
                isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow("is_admin")) == 1
            )
            Log.d("DBHelper", "User found: $user")
        } else {
            Log.d("DBHelper", "User not found for email: $email")
        }
        cursor.close()
        db.close()
        return user
    }

    fun getUserIdByEmail(email: String): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id FROM users WHERE email = '$email'", null)
        var userId = -1
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        }
        cursor.close()
        db.close()
        return userId
    }

    fun isEmailUnique(email: String): Boolean {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM users WHERE email = '$email'", null)
        val exists = result.moveToFirst()
        result.close()
        db.close()
        return !exists
    }

    fun getAllUsers(): List<User> {
        val users = mutableListOf<User>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users", null)

        if (cursor.moveToFirst()) {
            do {
                val user = User(
                    email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    login = cursor.getString(cursor.getColumnIndexOrThrow("login")),
                    phone = cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                    pass = CryptoUtils.decrypt(cursor.getString(cursor.getColumnIndexOrThrow("pass")), secretKey),
                    isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow("is_admin")) == 1
                )
                users.add(user)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return users
    }

    fun updateUserRole(userId: Int, isAdmin: Boolean) {
        val values = ContentValues()
        values.put("is_admin", if (isAdmin) 1 else 0)

        val db = this.writableDatabase
        db.update("users", values, "id = ?", arrayOf(userId.toString()))
        db.close()
    }

    fun addSale(itemId: Int, itemType: String, itemName: String, itemImage: String, itemDescription: String, price: Double, quantity: Int, saleDate: String): Int {
        val values = ContentValues().apply {
            put("item_id", itemId)
            put("item_type", itemType)
            put("item_name", itemName)
            put("item_image", itemImage)
            put("item_description", itemDescription)
            put("price", price)
            put("quantity", quantity)
            put("sale_date", saleDate)
        }

        val db = this.writableDatabase
        val saleId = db.insert("sales", null, values).toInt()
        db.close()
        return saleId
    }

    fun getSales(): List<Sale> {
        val sales = mutableListOf<Sale>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM sales", null)

        if (cursor.moveToFirst()) {
            do {
                val sale = Sale(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    itemId = cursor.getInt(cursor.getColumnIndexOrThrow("item_id")),
                    itemType = cursor.getString(cursor.getColumnIndexOrThrow("item_type")),
                    itemName = cursor.getString(cursor.getColumnIndexOrThrow("item_name")),
                    itemImage = cursor.getString(cursor.getColumnIndexOrThrow("item_image")),
                    itemDescription = cursor.getString(cursor.getColumnIndexOrThrow("item_description")),
                    price = cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                    quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                    saleDate = cursor.getString(cursor.getColumnIndexOrThrow("sale_date"))
                )
                sales.add(sale)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return sales
    }

    fun addComment(saleId: Int, comment: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("sale_id", saleId)
            put("comment", comment)
        }
        db.insert("comments", null, values)
        db.close()
    }

    fun getCommentsBySaleId(saleId: Int): List<Comment> {
        val db = this.readableDatabase
        val query = "SELECT * FROM comments WHERE sale_id = ?"
        val result = db.rawQuery(query, arrayOf(saleId.toString()))
        val comments = mutableListOf<Comment>()

        val idIndex = result.getColumnIndex("id")
        val saleIdIndex = result.getColumnIndex("sale_id")
        val commentIndex = result.getColumnIndex("comment")

        if (idIndex != -1 && saleIdIndex != -1 && commentIndex != -1) {
            if (result.moveToFirst()) {
                do {
                    val comment = Comment(
                        result.getInt(idIndex),
                        result.getInt(saleIdIndex),
                        result.getString(commentIndex)
                    )
                    comments.add(comment)
                } while (result.moveToNext())
            }
        } else {
            Log.e("DBHelper", "One or more columns not found in comments table")
        }

        result.close()
        db.close()
        return comments
    }

}
