package com.example.krmobil.products

import android.content.Context
import android.content.Intent
import android.util.Log
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
import com.example.krmobil.models.Tool
import com.example.krmobil.register.EditToolActivity
import com.example.krmobil.register.ReviewsActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ToolListAdapter(private var tools: List<Tool>, private val context: Context, private val isAdmin: Boolean) : RecyclerView.Adapter<ToolListAdapter.ToolViewHolder>() {

    class ToolViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.item_list_image)
        val name: TextView = view.findViewById(R.id.item_list_name)
        val description: TextView = view.findViewById(R.id.item_list_description)
        val price: TextView = view.findViewById(R.id.item_list_price)
        val edit: Button = view.findViewById(R.id.item_list_edit_button)
        val delete: Button = view.findViewById(R.id.item_list_delete_button)
        val buy: Button = view.findViewById(R.id.button_buy)
        val quantityEditText: EditText = view.findViewById(R.id.quantity_edit_text)
        val reviews: Button = view.findViewById(R.id.item_list_reviews_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.instrument_in_list, parent, false)
        return ToolViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tools.size
    }

    override fun onBindViewHolder(holder: ToolViewHolder, position: Int) {
        val tool = tools[position]
        holder.name.text = tool.name
        holder.description.text = tool.description
        holder.price.text = context.getString(R.string.item_price, tool.price)

        // Загрузка изображения с использованием Glide
        Glide.with(context)
            .load(getImageResourceId(tool.image)) // Если tool.image - это имя ресурса
            .into(holder.image)

        if (isAdmin) {
            holder.edit.visibility = View.VISIBLE
            holder.delete.visibility = View.VISIBLE
            holder.buy.visibility = View.GONE
            holder.quantityEditText.visibility = View.GONE
            holder.reviews.visibility = View.GONE

            holder.edit.setOnClickListener {
                val intent = Intent(context, EditToolActivity::class.java)
                intent.putExtra("toolId", tool.id)
                context.startActivity(intent)
            }

            holder.delete.setOnClickListener {
                val dbHelper = DBHelper(context, null)
                dbHelper.deleteTool(tool.id)
                tools = tools.filter { it.id != tool.id }
                notifyDataSetChanged()
            }
        } else {
            holder.edit.visibility = View.GONE
            holder.delete.visibility = View.GONE
            holder.buy.visibility = View.VISIBLE
            holder.quantityEditText.visibility = View.VISIBLE
            holder.reviews.visibility = View.VISIBLE

            holder.buy.setOnClickListener {
                val quantity = holder.quantityEditText.text.toString().toIntOrNull() ?: 1
                val dbHelper = DBHelper(context, null)
                val saleId = dbHelper.addSale(
                    tool.id,
                    "tool",
                    tool.name,
                    tool.image,
                    tool.description,
                    tool.price,
                    quantity,
                    getCurrentDate()
                )
                Toast.makeText(context, context.getString(R.string.buy_message, tool.name, quantity), Toast.LENGTH_SHORT).show()
                Log.d("ToolListAdapter", "Buy button clicked for tool: ${tool.name}, quantity: $quantity")

                // Передаем saleId в Intent для кнопки "Отзывы"
                holder.reviews.setOnClickListener {
                    val intent = Intent(context, ReviewsActivity::class.java)
                    intent.putExtra("saleId", saleId)
                    context.startActivity(intent)
                }
            }

            // Обработка нажатия на кнопку "Отзывы"
            holder.reviews.setOnClickListener {
                val intent = Intent(context, ReviewsActivity::class.java)
                intent.putExtra("saleId", tool.id) // Передаем saleId
                context.startActivity(intent)
            }
        }
    }

    fun updateTools(newTools: List<Tool>) {
        tools = newTools
        notifyDataSetChanged()
    }

    // Helper function to get current date
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun getImageResourceId(imageName: String): Int {
        return context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }
}
