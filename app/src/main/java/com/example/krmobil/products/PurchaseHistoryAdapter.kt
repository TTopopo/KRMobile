// PurchaseHistoryAdapter.kt
package com.example.krmobil.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.krmobil.R
import com.example.krmobil.models.Sale

class PurchaseHistoryAdapter(private var sales: List<Sale>) : RecyclerView.Adapter<PurchaseHistoryAdapter.SaleViewHolder>() {

    class SaleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.sale_item_name)
        val quantity: TextView = itemView.findViewById(R.id.sale_item_quantity)
        val saleDate: TextView = itemView.findViewById(R.id.sale_item_saledate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_purchase_history, parent, false)
        return SaleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SaleViewHolder, position: Int) {
        val sale = sales[position]
        holder.itemName.text = sale.itemType // Можно добавить логику для получения имени товара по itemId и itemType
        holder.quantity.text = sale.quantity.toString()
        holder.saleDate.text = sale.saleDate
    }

    override fun getItemCount(): Int {
        return sales.size
    }
}
