package com.example.krmobil.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.krmobil.R
import com.example.krmobil.models.Sale

class AdminSaleAdapter(private var sales: List<Sale>) : RecyclerView.Adapter<AdminSaleAdapter.SaleViewHolder>() {

    class SaleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.sale_item_name)
        val itemDescription: TextView = itemView.findViewById(R.id.sale_item_description)
        val itemPrice: TextView = itemView.findViewById(R.id.sale_item_price)
        val quantity: TextView = itemView.findViewById(R.id.sale_item_quantity)
        val saleDate: TextView = itemView.findViewById(R.id.sale_item_saledate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_purchase_history, parent, false)
        return SaleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SaleViewHolder, position: Int) {
        val sale = sales[position]
        holder.itemName.text = sale.itemName
        holder.itemDescription.text = "Описание: ${sale.itemDescription}"
        holder.itemPrice.text = "Цена: ${sale.price} руб."
        holder.quantity.text = "Количество: ${sale.quantity}"
        holder.saleDate.text = "Дата: ${sale.saleDate}"
    }

    override fun getItemCount(): Int {
        return sales.size
    }

    fun updateSales(newSales: List<Sale>) {
        sales = newSales
        notifyDataSetChanged()
    }
}
