package com.example.myapplication.Post

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PostFragment : Fragment(), PostAdapter.OnPostItemClickListener {

    private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var postAdapter1: PostAdapter
    private lateinit var postAdapter2: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post, container, false)

        // Floating Action Button 설정
        val addFloatingButton = view.findViewById<FloatingActionButton>(R.id.FB_post)
        addFloatingButton.setOnClickListener {
            startActivity(Intent(requireContext(), PostActivity::class.java))
        }

        // 툴바 설정
        val toolbar = view.findViewById<Toolbar>(R.id.post_toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24_white)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // RecyclerView 설정
        recyclerView1 = view.findViewById(R.id.recyclerView1)
        recyclerView2 = view.findViewById(R.id.recyclerView2)
        recyclerView1.layoutManager = LinearLayoutManager(requireContext())
        recyclerView2.layoutManager = LinearLayoutManager(requireContext())
        postAdapter1 = PostAdapter(this)
        postAdapter2 = PostAdapter(this)
        recyclerView1.adapter = postAdapter1
        recyclerView2.adapter = postAdapter2

        // Firestore에서 게시물 로드
        loadPostsFromFirestore()

        return view
    }

    private fun loadPostsFromFirestore() {
        val firestore = FirebaseFirestore.getInstance()
        val postsRef = firestore.collection("posts")
            .orderBy("time", Query.Direction.DESCENDING)

        postsRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // 오류 처리
                return@addSnapshotListener
            }

            val postList1 = mutableListOf<Post_Title>()
            val postList2 = mutableListOf<Post_Title>()

            snapshot?.documents?.forEach { doc ->
                val postid = doc.getString("postid") ?: ""
                val title = doc.getString("title") ?: ""
                val category = doc.getString("category") ?: ""
                val time = doc.getTimestamp("time") ?: Timestamp.now()
                val content = doc.getString("content") ?: ""
                val nickname = doc.getString("nickname") ?: ""
                val imageUrls = doc.get("imageUrl") as? List<String> ?: emptyList()


                val post = Post_Title(title, time, category, content, postid, nickname, imageUrls)
                if (category == "[공지]") {
                    postList1.add(post)
                } else {
                    postList2.add(post)
                }
            }

            // 데이터 업데이트
            postAdapter1.updateData(postList1.take(4))
            postAdapter2.updateData(postList2.take(5))
        }
    }

    override fun onPostItemClick(post: Post_Title) {
        // 아이템 클릭 시 해당 아이템의 데이터를 Post_ItemFragment로 전달
        val fragment = Post_ItemFragment.newInstance(post.title, post.content, post.time, post.category, post.postid, post.nickname, post.imageUrl)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container_main, fragment)
            .addToBackStack(null)
            .commit()
    }
}
