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
import com.example.krmobil.R
import com.example.krmobil.dbhelper.DBHelper
import com.example.krmobil.models.Material
import com.example.krmobil.register.EditMaterialActivity
import com.example.krmobil.register.ReviewsActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MaterialListAdapter(private var materials: List<Material>, private val context: Context, private val isAdmin: Boolean) : RecyclerView.Adapter<MaterialListAdapter.MaterialViewHolder>() {

    class MaterialViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.material_in_list, parent, false)
        return MaterialViewHolder(view)
    }

    override fun getItemCount(): Int {
        return materials.size
    }

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        val material = materials[position]
        holder.name.text = material.name
        holder.description.text = material.description
        holder.price.text = "${material.price} руб."

        val imageId = context.resources.getIdentifier(
            material.image,
            "drawable",
            context.packageName
        )

        holder.image.setImageResource(if (imageId != 0) imageId else R.drawable.ic_launcher_background)

        if (isAdmin) {
            holder.edit.visibility = View.VISIBLE
            holder.delete.visibility = View.VISIBLE
            holder.buy.visibility = View.GONE
            holder.quantityEditText.visibility = View.GONE
            holder.reviews.visibility = View.GONE

            holder.edit.setOnClickListener {
                val intent = Intent(context, EditMaterialActivity::class.java)
                intent.putExtra("materialId", material.id)
                context.startActivity(intent)
            }

            holder.delete.setOnClickListener {
                val dbHelper = DBHelper(context, null)
                dbHelper.deleteMaterial(material.id)
                materials = materials.filter { it.id != material.id }
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
                    material.id,
                    "material",
                    material.name,
                    material.image,
                    material.description,
                    material.price,
                    quantity,
                    getCurrentDate()
                )
                Toast.makeText(context, "Вы купили ${material.name} в количестве $quantity штук", Toast.LENGTH_SHORT).show()
                Log.d("MaterialListAdapter", "Buy button clicked for material: ${material.name}, quantity: $quantity")
            }

            holder.reviews.setOnClickListener {
                val intent = Intent(context, ReviewsActivity::class.java)
                intent.putExtra("itemId", material.id)
                intent.putExtra("itemType", "material")
                context.startActivity(intent)
            }
        }
    }

    fun updateMaterials(newMaterials: List<Material>) {
        materials = newMaterials
        notifyDataSetChanged()
    }

    // Helper function to get current date
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}
