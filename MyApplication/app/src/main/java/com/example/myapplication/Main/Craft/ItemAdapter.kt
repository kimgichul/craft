package com.example.myapplication.Main.Craft

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Main.Craft.cpu.Item

import com.example.myapplication.R
import com.squareup.picasso.Picasso

class ItemAdapter(private val itemList: List<Item>, private val listener: OnItemClickListener) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textView1: TextView = itemView.findViewById(R.id.textView1)
        val textView2: TextView = itemView.findViewById(R.id.textView2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.part_layout, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.textView1.text = currentItem.name
        holder.textView2.text = currentItem.price.toString() + "원"
        Picasso.get().load(currentItem.imageUrl).into(holder.imageView)

        holder.itemView.setOnClickListener {
            listener.onItemClick(currentItem)
        }
    }

    override fun getItemCount() = itemList.size

    interface OnItemClickListener {
        fun onItemClick(item: Item)
    }
}

