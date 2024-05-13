package com.example.myapplication.Post

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*

class PostActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: ImageAdapter
    private val selectedImages = ArrayList<String>()
    private val storage = Firebase.storage
    private val storageRef = storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        // Firebase 인스턴스 초기화
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // 뷰 초기화
        val buttonSubmit = findViewById<Button>(R.id.activity_post_bt_submit)
        val buttonCancel = findViewById<Button>(R.id.activity_post_bt_cancel)
        val postToolbar = findViewById<Toolbar>(R.id.post_toolbar)
        val imageViewAddImage = findViewById<ImageView>(R.id.activity_post_iv_plusimage)
        val recyclerViewImages = findViewById<RecyclerView>(R.id.activity_post_rv_list)

        // RecyclerView 설정
        adapter = ImageAdapter(selectedImages)
        recyclerViewImages.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewImages.adapter = adapter

        // 클릭 리스너 설정
        buttonSubmit.setOnClickListener {
            uploadImageToStorage()
        }

        buttonCancel.setOnClickListener {
            onBackPressed()
        }

        imageViewAddImage.setOnClickListener {
            openGallery()
        }

        // 툴바 뒤로가기 버튼 설정
        postToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    // 갤러리 열기
    private fun openGallery() {
        // 갤러리 열기 Intent
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        // 갤러리에서 이미지를 선택한 후 동작할 액티비티 결과 처리
        resultLauncher.launch(galleryIntent)
    }

    // 갤러리 Intent 결과 처리
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let { uri ->
                // 이미지 URI를 String 형태로 변환하여 리스트에 추가
                val imagePath = uri.toString()
                selectedImages.add(imagePath)
                // 어댑터에 변경 사항 알림
                adapter.notifyDataSetChanged()
            }
        } else {
            Toast.makeText(this, "이미지를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 이미지를 Firebase Storage에 업로드하고 Firestore에 저장
    private fun uploadImageToStorage() {
        if (selectedImages.isNotEmpty()) {
            // 모든 이미지 업로드를 추적하는 변수
            var uploadCount = 0
            val imageUrls = mutableListOf<String>()

            // 모든 이미지를 업로드하기 위한 반복문
            for ((index, imagePath) in selectedImages.withIndex()) {
                // 선택한 이미지가 있을 경우 업로드
                val imageUri = Uri.parse(imagePath)
                val imageFileName = "images/${UUID.randomUUID()}.jpg"
                val imageRef = storageRef.child(imageFileName)

                // 이미지 업로드
                imageRef.putFile(imageUri)
                    .addOnSuccessListener { taskSnapshot ->
                        // 업로드 성공 시 이미지의 다운로드 URL 가져오기
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            // 업로드한 이미지의 다운로드 URL을 리스트에 추가
                            imageUrls.add(uri.toString())
                            // 모든 이미지 업로드가 완료되었는지 확인
                            uploadCount++
                            if (uploadCount == selectedImages.size) {
                                // 모든 이미지가 업로드되었을 때 게시물 저장
                                savePostToFirestore(imageUrls)
                            }
                        }.addOnFailureListener { e ->
                            // 다운로드 URL 가져오기 실패 시 처리
                            Toast.makeText(this, "이미지 업로드 실패: $e", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        // 이미지 업로드 실패 시 처리
                        Toast.makeText(this, "이미지 업로드 실패: $e", Toast.LENGTH_SHORT).show()
                    }
            }
        } else {
            // 선택한 이미지가 없을 경우 처리
            savePostToFirestore(emptyList())
        }
    }


    // Firestore에 게시물 및 이미지 URL 저장하는 함수
    private fun savePostToFirestore(imageUrl: List<Any>) {
        val currentUser = auth.currentUser
        val editTextTitle = findViewById<EditText>(R.id.activity_post_et_title)
        val editTextContent = findViewById<EditText>(R.id.activity_post_et_content)
        val title = editTextTitle.text.toString().trim()
        val content = editTextContent.text.toString().trim()

        if (currentUser != null && title.isNotEmpty() && content.isNotEmpty()) {
            val userId = currentUser.uid
            val postId = UUID.randomUUID().toString() // 고유한 게시물 ID 생성
            val currentTime = FieldValue.serverTimestamp()

            // 사용자의 닉네임 가져오기
            val userRef = firestore.collection("users").document(userId)
            userRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    val nickname = documentSnapshot.getString("nickname") ?: ""

                    val post = hashMapOf(
                        "postid" to postId,
                        "title" to title,
                        "content" to content,
                        "userId" to userId,
                        "time" to currentTime, // 현재 시간 저장
                        "category" to "[자유]", // 카테고리 추가
                        "nickname" to nickname,
                        "imageUrl" to imageUrl // 이미지 URL 추가
                        // 필요한 경우 추가 필드 추가
                    )

                    // Firestore에 게시물 저장
                    firestore.collection("posts")
                        .document(postId)
                        .set(post)
                        .addOnSuccessListener {
                            Toast.makeText(this, "게시물이 성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show()
                            // 현재 액티비티 종료
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "게시물 저장 실패: $e", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "닉네임 가져오기 실패: $e", Toast.LENGTH_SHORT).show()
                }
        } else {
            // 사용자가 로그인하지 않았거나 제목 또는 내용이 비어 있을 경우 처리
            Toast.makeText(this, "게시물 제목과 내용을 입력하세요.", Toast.LENGTH_SHORT).show()
        }
    }
}
