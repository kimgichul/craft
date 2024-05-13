package com.example.myapplication.Post

import com.google.firebase.Timestamp

data class Comment(
    val userId: String = "",
    val content: String = "",
    val timestamp: Timestamp,
    val nickname: String = "",
    val commentId: String= ""
) {
    // 기본 생성자 추가
    constructor() : this("", "", Timestamp.now(), "", "")
}

