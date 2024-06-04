package com.example.myapplication.Main.Craft

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.Main.Craft.cpu.CpuActivity
import com.squareup.picasso.Picasso

class CraftFragment : Fragment() {

    private lateinit var button1: Button
    private val REQUEST_CODE_CPU_ACTIVITY = 101
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_craft, container, false)
        // Button1을 찾아 클릭 이벤트를 처리합니다.
        button1 = view.findViewById<Button>(R.id.button1)

        // 아이템 이미지를 표시할 ImageView를 찾습니다.
        val button1Image = view.findViewById<ImageView>(R.id.button1Image)
        val itemImageUrl = arguments?.getString("itemImageUrl")
        if (!itemImageUrl.isNullOrEmpty()) {
            Picasso.get().load(itemImageUrl).into(button1Image)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button1.setOnClickListener {
            Log.d("CraftFragment", "Button1 clicked")
            // 클릭 시 CpuActivity로 이동합니다.
            val intent = Intent(activity, CpuActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_CPU_ACTIVITY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CPU_ACTIVITY && resultCode == Activity.RESULT_OK) {
            val itemName = data?.getStringExtra("itemName")
            val itemPrice = data?.getLongExtra("itemPrice", 0)
            val itemImageUrl = data?.getStringExtra("itemImageUrl")
            // 받은 데이터를 처리합니다.
            if (!itemImageUrl.isNullOrEmpty()) {
                //Picasso.get().load(itemImageUrl).into(button1Image)
                button1.text = itemName
            }
        }
    }
}