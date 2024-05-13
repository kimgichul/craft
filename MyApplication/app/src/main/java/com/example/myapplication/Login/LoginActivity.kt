package com.example.myapplication.Login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var loginBtn: Button
    private lateinit var signupBtn: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 이미 로그인된 사용자가 있는지 확인
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            // 이미 로그인된 사용자가 있으면 메인 화면으로 이동
            startActivity(Intent(this, MainActivity::class.java))
            finish() // 현재 액티비티 종료
            return
        }

        setContentView(R.layout.activity_login)

        emailEt = findViewById(R.id.email_et)
        passwordEt = findViewById(R.id.pwd_et)
        loginBtn = findViewById(R.id.button)
        signupBtn = findViewById(R.id.sign_up_btn)

        loginBtn.setOnClickListener {
            val email = emailEt.text.toString()
            val password = passwordEt.text.toString()
            loginUser(email, password)
        }

        signupBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "아이디와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "이메일을 확인해주십시오", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // 현재 액티비티 종료
                } else {
                    Toast.makeText(this, "로그인 실패 아이디와 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
