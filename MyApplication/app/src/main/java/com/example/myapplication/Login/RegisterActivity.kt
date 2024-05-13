package com.example.myapplication.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var confirmPasswordEt: EditText
    private lateinit var nicknameEt: EditText
    private lateinit var registerBtn: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var firestoreDB: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        firestoreDB = FirebaseFirestore.getInstance()

        emailEt = findViewById(R.id.et_register_id)
        passwordEt = findViewById(R.id.et_register_pw)
        confirmPasswordEt = findViewById(R.id.et_register_pw_re)
        nicknameEt = findViewById(R.id.et_register_nickname)
        registerBtn = findViewById(R.id.btn_register_button)

        registerBtn.setOnClickListener {
            val email = emailEt.text.toString()
            val password = passwordEt.text.toString()
            val confirmPassword = confirmPasswordEt.text.toString()
            val nickname = nicknameEt.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && nickname.isNotEmpty()) {
                if (password == confirmPassword) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                                saveUserDataToFirestore(email, nickname) // Firestore에 사용자 이메일과 닉네임 저장
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "이메일과 비밀번호, 닉네임을 모두 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun saveUserDataToFirestore(email: String, nickname: String) {
        // 현재 인증된 사용자의 ID 가져오기
        val userId = auth.currentUser?.uid

        if (userId != null) {
            // Firestore에 저장할 데이터
            val userData = hashMapOf(
                "email" to email,
                "nickname" to nickname
                // 다른 사용자 정보 필드를 추가할 수 있습니다
            )

            // Firestore의 "users" 컬렉션에 현재 사용자의 ID 아래에 데이터를 저장합니다.
            firestoreDB.collection("users").document(userId)
                .set(userData)
                .addOnSuccessListener {
                    // 성공적으로 데이터가 저장된 경우
                    Log.d(TAG, "DocumentSnapshot written with ID: $userId")
                }
                .addOnFailureListener { e ->
                    // 데이터 저장 중에 오류가 발생한 경우
                    Log.w(TAG, "Error writing document", e)
                }
        } else {
            Log.e(TAG, "User ID is null")
        }
    }

    companion object {
        private const val TAG = "Register"
    }
}
