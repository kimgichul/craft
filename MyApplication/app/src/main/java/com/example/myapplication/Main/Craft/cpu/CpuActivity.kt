package com.example.myapplication.Main.Craft.cpu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TabHost
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Main.Craft.CraftFragment
import com.example.myapplication.Main.Craft.ItemAdapter
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class CpuActivity : AppCompatActivity(), ItemAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: ItemAdapter
    private val itemList = mutableListOf<Item>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cpu)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        itemAdapter = ItemAdapter(itemList, this)
        recyclerView.adapter = itemAdapter

        fetchData()
    }

    override fun onItemClick(item: Item) {
        // 아이템 클릭 시 CraftFragment로 아이템 정보를 전달합니다.
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val craftFragment = CraftFragment()

        // 아이템 정보를 CraftFragment로 전달합니다.
        val resultIntent = Intent()
        resultIntent.putExtra("itemName", item.name)
        resultIntent.putExtra("itemPrice", item.price)
        resultIntent.putExtra("itemImageUrl", item.imageUrl)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun fetchData() {
        db.collection("parts")
            .whereEqualTo("part", "CPU")
            .get()
            .addOnSuccessListener { result ->
                itemList.clear()
                for (document in result) {
                    val item = document.toObject(Item::class.java)
                    itemList.add(item)
                }
                itemAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // 에러 처리
            }
    }


}

data class Item(
    val name: String = "",
    val price: Long = 0,
    val imageUrl: String = ""
)