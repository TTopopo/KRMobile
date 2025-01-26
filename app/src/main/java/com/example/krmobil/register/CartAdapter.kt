package com.example.krmobil.register

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.krmobil.R
import com.example.krmobil.dbhelper.DBHelper
import com.example.krmobil.models.Sale

class CartAdapter(
    private var sales: List<Sale>,
    private val context: Context,
    private val userLogin: String // Добавьте параметр userLogin
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.cart_item_image)
        val name: TextView = view.findViewById(R.id.cart_item_name)
        val price: TextView = view.findViewById(R.id.cart_item_price)
        val quantity: TextView = view.findViewById(R.id.cart_item_quantity) // Добавьте TextView для количества
        val comment: EditText = view.findViewById(R.id.cart_item_comment)
        val submitComment: Button = view.findViewById(R.id.cart_item_submit_comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sales.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val sale = sales[position]
        holder.name.text = sale.itemName
        holder.price.text = "Цена: ${sale.price} руб."
        holder.quantity.text = "Количество: ${sale.quantity}" // Установите количество

        // Загрузка изображения с использованием Glide
        Glide.with(context)
            .load(getImageResourceId(sale.itemImage)) // Если sale.itemImage - это имя ресурса
            .into(holder.image)

        holder.submitComment.setOnClickListener {
            val comment = holder.comment.text.toString()
            if (comment.isNotEmpty()) {
                val dbHelper = DBHelper(context, null)
                dbHelper.addComment(sale.id, userLogin, comment) // Передаем userLogin
                Toast.makeText(context, "Комментарий отправлен", Toast.LENGTH_SHORT).show()
                holder.comment.setText("")
            } else {
                Toast.makeText(context, "Пожалуйста, введите комментарий", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getImageResourceId(imageName: String): Int {
        return context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }
}
