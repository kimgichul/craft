package com.example.myapplication.Post

import com.google.firebase.Timestamp

data class Post_Title(
    val title: String,
    val time: Timestamp,
    val category: String,
    val content: String,
    val postid: String,
    val nickname: String,
    val imageUrl: List<String>
    // 다른 필드들도 추가할 수 있습니다
)
