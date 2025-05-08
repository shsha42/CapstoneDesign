package com.codebly.zibro.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.codebly.zibro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText editTextEmail, editTextPassword;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        // FirebaseAuth 인스턴스 초기화
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();  // Firestore 인스턴스 초기화

        // XML에서 이메일과 비밀번호 입력 필드를 연결
        editTextEmail = findViewById(R.id.editTextText2);  // 이메일 입력 필드
        editTextPassword = findViewById(R.id.editTextText3);  // 비밀번호 입력 필드

        // 회원가입 버튼 클릭 시 동작
        findViewById(R.id.imageView31).setOnClickListener(view -> createUser());

    }

    private void createUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "이메일과 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        // 비밀번호 길이 확인
        if (password.length() < 6) {
            Toast.makeText(SignUpActivity.this, "비밀번호는 최소 6자 이상이어야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Firebase Auth로 회원가입
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // 회원가입 성공
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Firestore에 사용자 정보 저장
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("email", email);
                            userData.put("friends", new ArrayList<String>());  // 빈 친구 목록
                            userData.put("friendRequests", new ArrayList<String>());  // 빈 친구 요청 목록

                            // Firestore에 사용자 정보 저장
                            db.collection("Users").document(user.getUid())
                                    .set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "Firestore에 사용자 정보 저장 성공");

                                        // 로그인 화면으로 이동
                                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();  // 현재 액티비티 종료
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Firestore에 사용자 정보 저장 실패: " + e.getMessage());
                                        Toast.makeText(SignUpActivity.this, "Firestore에 사용자 정보 저장 실패", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        // 회원가입 실패
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            // 이메일이 이미 사용 중인 경우
                            Toast.makeText(SignUpActivity.this, "해당 이메일은 이미 사용 중입니다. \n 다른 이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            // 그 외 다른 오류 처리
                            Toast.makeText(SignUpActivity.this, "회원가입 실패: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}