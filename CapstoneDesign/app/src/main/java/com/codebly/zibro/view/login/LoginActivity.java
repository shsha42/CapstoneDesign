package com.codebly.zibro.view.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.ImageView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codebly.zibro.view.home.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import com.codebly.zibro.R;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // FirebaseAuth 인스턴스 초기화
        auth = FirebaseAuth.getInstance();

        // UI 요소 참조
        EditText emailField = findViewById(R.id.email_editText);  // 이메일 입력 필드
        EditText passwordField = findViewById(R.id.password_editText);  // 비밀번호 입력 필드
        ImageView loginButton = findViewById(R.id.login_button);  // 로그인 버튼

        // 로그인 버튼 클릭 시 Firebase 인증을 통해 로그인
        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();

            if (!email.isEmpty() && !password.isEmpty()) {
                // Firebase로 로그인 요청
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // 로그인 성공 -> MainActivity로 이동
                                FirebaseUser user = auth.getCurrentUser();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // 로그인 실패 처리
                                Toast.makeText(LoginActivity.this, "로그인 실패: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(LoginActivity.this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
