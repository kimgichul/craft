package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation_main)

        // 네비게이션 컨트롤러 가져오기
        val navController = supportFragmentManager.findFragmentById(R.id.container_main)?.findNavController()

        // 바텀 네비게이션과 네비게이션 컨트롤러 연결
        bottomNavigationView.setupWithNavController(navController!!)

        // 바텀 네비게이션 아이템 선택 이벤트 리스너 설정
        bottomNavigationView.setOnItemSelectedListener { item ->
            // 모든 버튼 클릭 시 네비게이션 그래프에 정의된 대로 해당 프래그먼트로 이동
            navController.navigate(item.itemId)
            true
        }
    }
}

