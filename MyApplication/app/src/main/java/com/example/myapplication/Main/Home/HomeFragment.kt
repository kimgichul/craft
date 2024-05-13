package com.example.myapplication.Main.Home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Guide.GuideActivity
import com.example.myapplication.Item.ItemActivity
import com.example.myapplication.Post.PostAdapter
import com.example.myapplication.Post.Post_ItemFragment
import com.example.myapplication.Post.Post_Title
import com.example.myapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeFragment : Fragment() {

    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Floating Action Button 초기화 및 클릭 리스너 설정
        view.findViewById<FloatingActionButton>(R.id.addFloatingButton).setOnClickListener {
            startActivity(Intent(requireContext(), ItemActivity::class.java))
        }

        view.findViewById<FloatingActionButton>(R.id.FloatingButton).setOnClickListener {
            startActivity(Intent(requireContext(), GuideActivity::class.java))
        }

        // 게시판 이미지 클릭 리스너 설정
        view.findViewById<ImageView>(R.id.img_post).setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_postFragment)
        }

        // RecyclerView 초기화
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView1)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Adapter 초기화
        postAdapter = PostAdapter(object : PostAdapter.OnPostItemClickListener {
            override fun onPostItemClick(post: Post_Title) {
                val fragment = Post_ItemFragment.newInstance(post.title, post.content, post.time, post.category, post.postid, post.nickname, post.imageUrl)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container_main, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        })
        recyclerView.adapter = postAdapter

        // Firestore에서 공지사항을 가져와서 RecyclerView에 표시
        loadNoticeFromFirestore()

        return view
    }

    private fun loadNoticeFromFirestore() {
        val firestore = FirebaseFirestore.getInstance()
        val postsRef = firestore.collection("posts").orderBy("time", Query.Direction.DESCENDING)

        postsRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Handle error
                return@addSnapshotListener
            }

            val noticeList = mutableListOf<Post_Title>()

            snapshot?.documents?.forEach { doc ->
                val title = doc.getString("title") ?: ""
                val content = doc.getString("content") ?: ""
                val category = doc.getString("category") ?: ""
                val time = doc.getTimestamp("time") ?: Timestamp.now()
                val postid = doc.getString("postid") ?: ""
                val nickname = doc.getString("nickname") ?: ""
                val imageUrls = doc.get("imageUrl") as? List<String> ?: emptyList()


                if (category == "[공지]") {
                    noticeList.add(Post_Title(title, time, category, content, postid, nickname, imageUrls))
                }
            }

            // RecyclerView에 데이터 설정
            postAdapter.updateData(noticeList.take(3))
        }
    }
}
