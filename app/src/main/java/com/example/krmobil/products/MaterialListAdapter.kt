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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.material_in_list, parent, false)
        return MaterialViewHolder(view)
    }

    override fun getItemCount(): Int {
        return materials.size
    }

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        holder.name.text = materials[position].name
        holder.description.text = materials[position].description
        holder.price.text = "${materials[position].price} руб."

        val imageId = context.resources.getIdentifier(
            materials[position].image,
            "drawable",
            context.packageName
        )

        holder.image.setImageResource(if (imageId != 0) imageId else R.drawable.ic_launcher_background)

        if (isAdmin) {
            holder.edit.visibility = View.VISIBLE
            holder.delete.visibility = View.VISIBLE
            holder.buy.visibility = View.GONE
            holder.quantityEditText.visibility = View.GONE

            holder.edit.setOnClickListener {
                val intent = Intent(context, EditMaterialActivity::class.java)
                intent.putExtra("materialId", materials[position].id)
                context.startActivity(intent)
            }

            holder.delete.setOnClickListener {
                val dbHelper = DBHelper(context, null)
                dbHelper.deleteMaterial(materials[position].id)
                materials = materials.filter { it.id != materials[position].id }
                notifyDataSetChanged()
            }
        } else {
            holder.edit.visibility = View.GONE
            holder.delete.visibility = View.GONE
            holder.buy.visibility = View.VISIBLE
            holder.quantityEditText.visibility = View.VISIBLE

            holder.buy.setOnClickListener {
                val quantity = holder.quantityEditText.text.toString().toIntOrNull() ?: 1
                val dbHelper = DBHelper(context, null)
                dbHelper.addSale(materials[position].id, "material", quantity, getCurrentDate())
                Toast.makeText(context, "Вы купили ${materials[position].name} в количестве $quantity штук", Toast.LENGTH_SHORT).show()
                Log.d("MaterialListAdapter", "Buy button clicked for material: ${materials[position].name}, quantity: $quantity")
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
