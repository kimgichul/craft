package com.example.myapplication.Main.Craft.cpu

import I5Fragment
import I6Fragment
import I7Fragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.ViewPager
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityCpuBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class CpuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cpu)

        val NavigationView = findViewById<BottomNavigationView>(R.id.navigation_cpu)
        val navController = supportFragmentManager.findFragmentById(R.id.container_cpu)?.findNavController()
        NavigationView.setupWithNavController(navController!!)

        NavigationView.setOnItemSelectedListener { item ->
            // 모든 버튼 클릭 시 네비게이션 그래프에 정의된 대로 해당 프래그먼트로 이동
            navController.navigate(item.itemId)
            true
        }


        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        val adapter = MyPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter
    }
}

class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> I5Fragment()
            1 -> I6Fragment()
            2 -> I7Fragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun getCount(): Int {
        return 3
    }
}