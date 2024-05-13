package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.Login.LoginActivity

class IconActivity : AppCompatActivity() {
    private val PERMISSION_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_icon)

        // SDK 버전에 따라 권한 확인 및 다음 액티비티로 이동
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            checkStoragePermission()
        } else {
            checkMediaImagesPermission()
        }
    }

    // 스토리지 읽기 권한 확인
    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            navigateToNextActivity()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    // 미디어 이미지 읽기 권한 확인
    private fun checkMediaImagesPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            navigateToNextActivity()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    // 권한 요청 결과 처리
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                navigateToNextActivity()
            } else {
                // 권한이 거부되었을 때 처리
                // 예를 들어 다이얼로그를 표시하고 사용자에게 권한이 필요함을 안내하는 등의 작업 수행 가능
                // 여기서는 간단히 finish()를 호출하여 액티비티를 종료합니다.
                finish()
            }
        }
    }

    // 다음 액티비티로 이동
    private fun navigateToNextActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // 현재 액티비티 종료
        }, 1500) // 1.5초(1500밀리초) 후에 실행
    }
}
