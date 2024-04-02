package com.example.myapplication.Main.Post

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.Post.PostActivity
import com.example.myapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post, container, false)

        val addFloatingButton = view.findViewById<FloatingActionButton>(R.id.FB_post)

        // 플로팅 액션 버튼 클릭 리스너 설정
        addFloatingButton.setOnClickListener {
            // itemFragment로 이동하는 코드
            val intent = Intent(requireContext(), PostActivity::class.java)
            startActivity(intent)
        }

        // Find the toolbar
        val toolbar = view.findViewById<Toolbar>(R.id.post_toolbar)

        // Set navigation icon
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24_white)

        // Set navigation click listener to navigate back to HomeFragment using NavController
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        return view
    }
}
