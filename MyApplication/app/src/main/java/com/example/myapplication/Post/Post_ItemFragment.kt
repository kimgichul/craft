package com.example.myapplication.Post

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class Post_ItemFragment : Fragment() {
    private lateinit var textViewTitle: TextView
    private lateinit var textViewContent: TextView
    private lateinit var textViewTime: TextView
    private lateinit var textViewTag: TextView
    private lateinit var textViewWriter: TextView // 작성자 텍스트뷰 추가
    private lateinit var imageButton1: ImageView
    private lateinit var imageButton2: ImageView
    private lateinit var imageButton3: ImageView
    private lateinit var currentPostId: String
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var commentList: MutableList<Comment>
    private lateinit var recyclerViewComments: RecyclerView
    private lateinit var horizontalRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_post_item, container, false)
        textViewTitle = view.findViewById(R.id.fragment_post_item_tv_title)
        textViewContent = view.findViewById(R.id.fragment_post_item_tv_content)
        textViewTime = view.findViewById(R.id.fragment_post_item_tv_time)
        textViewTag = view.findViewById(R.id.fragment_post_item_tv_tag)
        textViewWriter = view.findViewById(R.id.fragment_post_item_tv_writer)
        imageButton1 = view.findViewById(R.id.imageButton1)
        imageButton2 = view.findViewById(R.id.imageButton2)
        imageButton3 = view.findViewById(R.id.imageButton3)
        recyclerViewComments = view.findViewById(R.id.fragment_post_item_rc1)
        horizontalRecyclerView = view.findViewById(R.id.fragment_post_item_rc2) // 추가된 부분


        val title = arguments?.getString("title", "")
        val content = arguments?.getString("content", "")
        val time = arguments?.get("time") as? Timestamp ?: Timestamp.now()
        val tag = arguments?.getString("tag", "")
        val nickname = arguments?.getString("nickname", "") ?: ""
        currentPostId = arguments?.getString("postid", "") ?: ""
        val imageUrls = arguments?.getStringArrayList("imageUrl") ?: ArrayList()

        textViewTitle.text = title
        textViewContent.text = content
        textViewTime.text = formatTimestamp(time)
        textViewTag.text = tag
        textViewWriter.text = nickname // 작성자 텍스트뷰에 닉네임 설정

        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.post_toolbar)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        if (tag == "[공지]") {
            imageButton1.visibility = View.INVISIBLE
            imageButton2.visibility = View.INVISIBLE
            imageButton3.visibility = View.INVISIBLE
            imageButton1.isEnabled = false
            imageButton2.isEnabled = false
            imageButton3.isEnabled = false
        } else {
            // 태그가 "[공지]"가 아니면서 imageButton1과 imageButton2를 활성화
            imageButton1.setOnClickListener {
                addToFavorites()
            }

            imageButton2.setOnClickListener {
                addToLikes()
                addToLikeMembers()
            }
        }

        val submitCommentButton = view.findViewById<ImageView>(R.id.fragment_post_item_iv_SubmitComment)
        val commentEditText = view.findViewById<EditText>(R.id.fragment_post_item_et_Comment)

        submitCommentButton.setOnClickListener {
            val commentText = commentEditText.text.toString()
            if (commentText.isNotEmpty()) {
                addComment(commentText, nickname)
                // 댓글이 성공적으로 추가되면 에딧 텍스트뷰의 내용을 비웁니다.
                commentEditText.text.clear()
            } else {
                Toast.makeText(requireContext(), "댓글을 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

        val imageItemAdapter = ImageAdapter(imageUrls)
        horizontalRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        horizontalRecyclerView.adapter = imageItemAdapter

        Log.d("Post_ItemFragment", "Title: $title")
        Log.d("Post_ItemFragment", "Image URLs: $imageUrls")
        Log.d("Post_ItemFragment", "nickname: $nickname")
        Log.d("Post_ItemFragment", "time: $time")

        return view
    }

    private fun formatTimestamp(timestamp: Timestamp): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(timestamp.toDate())
    }

    private fun addToFavorites() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val favoritesRef = FirebaseFirestore.getInstance().collection("users")
            .document(userId).collection("favorites_post").document("myfavorite")
        data class MyFavorite(
            val postIds: List<String> = mutableListOf()
        ) {
            // 기본 생성자 추가
            constructor() : this(mutableListOf())
        }

        favoritesRef.get().addOnSuccessListener { documentSnapshot ->
            val myFavorite = documentSnapshot.toObject(MyFavorite::class.java)

            val postIds = myFavorite?.postIds?.toMutableList() ?: mutableListOf()

            if (!postIds.contains(currentPostId)) {
                postIds.add(currentPostId)
                val updatedFavorite = MyFavorite(postIds)

                favoritesRef.set(updatedFavorite).addOnSuccessListener {
                    Toast.makeText(requireContext(), "즐겨찾기에 추가되었습니다!", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "즐겨찾기 추가 실패: $e", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "이미 즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(requireContext(), "즐겨찾기 추가 실패: $e", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addToLikes() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val likesRef = FirebaseFirestore.getInstance().collection("users")
            .document(userId).collection("like_post").document("mylike")
        data class MyLike(
            val likedIds: List<String> = mutableListOf()
        ) {
            // 기본 생성자 추가
            constructor() : this(mutableListOf())
        }

        likesRef.get().addOnSuccessListener { documentSnapshot ->
            val myLike = documentSnapshot.toObject(MyLike::class.java)

            val likedIds = myLike?.likedIds?.toMutableList() ?: mutableListOf()

            if (!likedIds.contains(currentPostId)) {
                likedIds.add(currentPostId)
                val updatedLike = MyLike(likedIds)

                likesRef.set(updatedLike).addOnSuccessListener {
                    Toast.makeText(requireContext(), "좋아요를 눌렀습니다!", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "좋아요 추가 실패: $e", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "이미 좋아요를 누르셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(requireContext(), "좋아요 추가 실패: $e", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addToLikeMembers() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val postRef = FirebaseFirestore.getInstance().collection("posts").document(currentPostId)
        val likedMemberRef = postRef.collection("liked_member").document("postliked")
        data class PostLiked (
            val membersIds: List<String> = mutableListOf()
        ) {
            // 기본 생성자 추가
            constructor() : this(mutableListOf())
        }
        likedMemberRef.get().addOnSuccessListener { likedMemberSnapshot ->
            val likedMemberData = likedMemberSnapshot.toObject(PostLiked::class.java)

            val postLikedMembers = likedMemberData?.membersIds?.toMutableList() ?: mutableListOf()

            if (!postLikedMembers.contains(userId)) {
                postLikedMembers.add(userId)
                val updatedPostLiked = PostLiked(postLikedMembers)

                likedMemberRef.set(updatedPostLiked).addOnSuccessListener {
                    Log.d("AddToLikeMembers", "좋아요를 눌렀습니다!")
                }.addOnFailureListener { e ->
                    Log.e("AddToLikeMembers", "좋아요 추가 실패: $e")
                }
            } else {
                Log.d("AddToLikeMembers", "이미 좋아요를 누르셨습니다.")
            }
        }.addOnFailureListener { e ->
            Log.e("AddToLikeMembers", "좋아요 추가 실패: $e")
        }
    }

    private fun addComment(commentText: String, nickname: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val commentRef = FirebaseFirestore.getInstance().collection("posts")
            .document(currentPostId).collection("comments").document()

        val commentId = commentRef.id // 새로운 댓글의 문서 ID를 가져옵니다.

        val commentData = hashMapOf(
            "userId" to userId,
            "content" to commentText,
            "timestamp" to FieldValue.serverTimestamp(),
            "nickname" to nickname,
            "commentId" to commentId // 댓글의 문서 ID를 저장합니다.
        )

        commentRef.set(commentData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                // 댓글이 추가되면 RecyclerView를 다시로드하여 새 댓글을 보여줍니다.
                loadComments()
            }
            .addOnFailureListener { e ->
                Log.e("AddComment", "Error adding document", e)
                Toast.makeText(requireContext(), "댓글 등록에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 초기화
        recyclerViewComments.layoutManager = LinearLayoutManager(requireContext())
        commentList = mutableListOf()
        commentAdapter = CommentAdapter(commentList)
        recyclerViewComments.adapter = commentAdapter

        // 해당 포스트의 댓글 가져오기
        loadComments()
    }

    private fun loadComments() {
        val postRef = FirebaseFirestore.getInstance().collection("posts").document(currentPostId)
        postRef.collection("comments")
            .orderBy("timestamp") // 시간을 기준으로 오름차순 정렬
            .get()
            .addOnSuccessListener { documents ->
                commentList.clear()
                for (document in documents) {
                    val comment = document.toObject(Comment::class.java)
                    commentList.add(comment)
                }
                if (commentList.isEmpty()) {
                    // 댓글이 없는 경우, 댓글을 로드하는 RecyclerView를 숨깁니다.
                    recyclerViewComments.visibility = View.GONE
                    // 여기에 댓글이 없음을 나타내는 다른 UI 요소를 처리할 수 있습니다.
                } else {
                    // 댓글이 있는 경우, RecyclerView를 보여줍니다.
                    recyclerViewComments.visibility = View.VISIBLE
                    commentAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Post_ItemFragment", "Error getting comments", exception)
            }

        imageButton3.setOnClickListener {
            // AlertDialog 빌더 생성
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("게시글 삭제")
            builder.setMessage("게시글을 삭제 하시겠습니까?")

            // 확인 버튼 설정
            builder.setPositiveButton("확인") { dialog, which ->
                // 확인 버튼을 눌렀을 때 수행할 작업
                deletePost(currentPostId)
            }

            // 취소 버튼 설정
            builder.setNegativeButton("취소") { dialog, which ->
                // 취소 버튼을 눌렀을 때 수행할 작업을 여기에 추가합니다.
                Toast.makeText(requireContext(), "취소 버튼이 눌렸습니다.", Toast.LENGTH_SHORT).show()
            }

            // AlertDialog 생성 및 표시
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

    // 파이어스토어에서 해당 포스트 삭제하는 함수
    private fun deletePost(currentPostId: String) {
        val postRef = FirebaseFirestore.getInstance().collection("posts").document(currentPostId)
        postRef.delete()
            .addOnSuccessListener {
                // 삭제 성공 시
                Toast.makeText(requireContext(), "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressed() // 포스트 삭제 후 이전 화면으로 이동
            }
            .addOnFailureListener { e ->
                // 삭제 실패 시
                Toast.makeText(requireContext(), "게시글 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
                Log.e("deletePost", "Error deleting document", e)
            }
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String, content: String, time: Timestamp, tag: String, postid: String, nickname: String, imageUrl: List<String>) =
            Post_ItemFragment().apply {
                arguments = Bundle().apply {
                    putString("title", title)
                    putString("content", content)
                    putString("time", time.toString())
                    putString("tag", tag)
                    putString("postid",postid)
                    putString("nickname",nickname)
                    putStringArrayList("imageUrl", ArrayList(imageUrl))
                }
            }
    }

}
