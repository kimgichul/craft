package com.example.myapplication.Main.Home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.Guide.GuideActivity
import com.example.myapplication.Item.ItemActivity
import com.example.myapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_home.xml을 인플레이트하여 뷰 생성
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // 플로팅 액션 버튼 찾기
        val addFloatingButton = view.findViewById<FloatingActionButton>(R.id.addFloatingButton)
        val floatingButton = view.findViewById<FloatingActionButton>(R.id.FloatingButton)

        // 플로팅 액션 버튼 클릭 리스너 설정
        addFloatingButton.setOnClickListener {
            // itemFragment로 이동하는 코드
            val intent = Intent(requireContext(), ItemActivity::class.java)
            startActivity(intent)
        }

        // 두 번째 플로팅 액션 버튼 클릭 리스너 설정
        floatingButton.setOnClickListener {
            val intent = Intent(requireContext(), GuideActivity::class.java)
            startActivity(intent)
        }

        val imageView = view.findViewById<ImageView>(R.id.img_post)
            // 이미지 버튼 클릭 리스너 설정
        imageView.setOnClickListener {
            // postFragment로 이동하는 코드
            findNavController().navigate(R.id.action_navigation_home_to_postFragment)
        }

        return view
    }
}
