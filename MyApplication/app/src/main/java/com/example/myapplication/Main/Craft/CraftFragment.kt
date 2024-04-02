package com.example.myapplication.Main.Craft

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.myapplication.Item.ItemActivity
import com.example.myapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.navigation.fragment.findNavController
import com.example.myapplication.Guide.GuideActivity

class CraftFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_craft, container, false)

        val button1 = view.findViewById<Button>(R.id.button1)
        val button2 = view.findViewById<Button>(R.id.button2)
        val button3 = view.findViewById<Button>(R.id.button3)
        val button4 = view.findViewById<Button>(R.id.button4)
        val button5 = view.findViewById<Button>(R.id.button5)
        val button6 = view.findViewById<Button>(R.id.button6)
        val button7 = view.findViewById<Button>(R.id.button7)
        val button8 = view.findViewById<Button>(R.id.button8)

        button1.setOnClickListener {
            val intent = Intent(requireContext(), GuideActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}