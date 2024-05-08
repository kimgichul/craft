package com.example.myapplication.Main.Craft

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.myapplication.R
import com.example.myapplication.Main.Craft.cpu.CpuActivity

class CraftFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_craft.xml 레이아웃을 inflate하여 View 객체를 생성합니다.
        val view = inflater.inflate(R.layout.fragment_craft, container, false)

        // Button1을 findViewById() 메서드를 사용하여 참조합니다.
        val button1 = view.findViewById<Button>(R.id.button1)

        // Button1에 클릭 리스너를 설정합니다.
        button1.setOnClickListener {
            // Button1이 클릭되었을 때 실행되는 코드를 작성합니다.
            // Intent를 사용하여 CPUActivity로 이동하는 코드를 작성합니다.
            val intent = Intent(activity, CpuActivity::class.java)
            startActivity(intent)
        }

        // 생성된 View 객체를 반환합니다.
        return view
    }


}