package com.codebly.zibro.view.home.menu.alarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.codebly.zibro.R;
import com.codebly.zibro.databinding.ActivityNotiBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotiActivity extends AppCompatActivity {

    private RecyclerView notificationRecyclerView;
    private NotificationAdapter adapter;
    private ArrayList<NotificationItem> notificationList;

    private ActivityNotiBinding binding;
    private NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti);

        binding = ActivityNotiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // RecyclerView 초기화
        notificationRecyclerView = findViewById(R.id.notificationRecyclerView);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // 어댑터 초기화
        notificationList = new ArrayList<>();
        adapter = new NotificationAdapter(notificationList);
        notificationRecyclerView.setAdapter(adapter);

        // Intent로 전달된 알림이 있을 경우 업로드
        String dateTime = getIntent().getStringExtra("dateTime");
        String message = getIntent().getStringExtra("message");
        if (dateTime != null && message != null) {
            uploadNotification(dateTime, message);
        }

        // RecyclerView 초기화
        notificationAdapter = new NotificationAdapter(notificationList);
        binding.notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.notificationRecyclerView.setAdapter(notificationAdapter);

        // 텍스트뷰 및 알림 설정
        updateNotificationDisplay();

        // 예시: 버튼 클릭 시 setXMLToggle 호출
        binding.viewTv.setOnClickListener(v -> setXMLToggle(true));
        binding.subScriberTv.setOnClickListener(v -> setXMLToggle(false));


        // 예시: 버튼 클릭 시 setXMLToggle 호출
        binding.viewTv.setOnClickListener(v -> setXMLToggle(true));
        binding.subScriberTv.setOnClickListener(v -> setXMLToggle(false));
    }

    private void loadNotificationsFromPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("notiData", Context.MODE_PRIVATE);
        String json = sharedPref.getString("notifications", "[]");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<NotificationItem>>() {}.getType();
        ArrayList<NotificationItem> notifications = gson.fromJson(json, type);

        notificationList.clear(); // 현재 목록 비우기
        notificationList.addAll(notifications); // 로드한 알림 추가
        adapter.notifyDataSetChanged(); // 어댑터에 데이터 변경 알림
    }

    // 알림창에 업로드하는 메서드
    public void uploadNotification(String dateTime, String message) {
        Log.d("NotiActivity", "uploadNotification called with message: " + message);
        NotificationItem newNotification = new NotificationItem(dateTime, message);
        notificationList.add(newNotification);

        // Log the current state of the notification list
        Log.d("NotiActivity", "Current notifications: " + notificationList.toString());

        // Notify the adapter that the data has changed
        adapter.notifyItemInserted(notificationList.size() - 1);
        adapter.notifyDataSetChanged(); // 전체 업데이트
    }

    private void updateNotificationDisplay() {
        // SharedPreferences에서 데이터 불러오기
        SharedPreferences sharedPref = getSharedPreferences("notiData", Context.MODE_PRIVATE);
        String json = sharedPref.getString("notifications", "[]"); // 알림 목록을 JSON으로 불러오기

        // Gson을 사용하여 JSON을 ArrayList로 변환
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<NotificationItem>>() {}.getType();
        notificationList.clear(); // 기존 목록 초기화
        notificationList.addAll(gson.fromJson(json, type)); // 알림 목록 추가
        notificationAdapter.notifyDataSetChanged(); // RecyclerView 업데이트
    }

    private void addNotification(String message) {
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // 최대 30개의 기록을 유지
        if (notificationList.size() >= 30) {
            notificationList.remove(notificationList.size() - 1); // 가장 오래된 기록 삭제
        }

        // 새 알림 추가
        notificationList.add(0, new NotificationItem(currentTime, message));
        notificationAdapter.notifyItemInserted(0);
        binding.notificationRecyclerView.scrollToPosition(0); // 스크롤 위치를 가장 위로 이동

        saveNotificationsToPreferences(); // 알림을 SharedPreferences에 저장
    }


    private void saveNotificationsToPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("notiData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(notificationList); // ArrayList를 JSON으로 변환
        editor.putString("notifications", json); // 저장
        editor.apply();
        Log.d("NotiActivity", "Notifications saved: " + json); // 로그 추가
    }

    private void setXMLToggle(boolean isViewClicked) {
        if (isViewClicked) {
            // 알림 선택 시
            binding.subScriberTv.setTextColor(ContextCompat.getColor(this, R.color.main));
            binding.subScriberTv.setBackgroundResource(0);
            binding.viewTv.setTextColor(ContextCompat.getColor(this, R.color.white));
            binding.viewTv.setBackgroundResource(R.drawable.item_bg_on);

            // RecyclerView 보이기
            binding.notificationRecyclerView.setVisibility(View.VISIBLE);
            binding.subnotificationRecyclerView.setVisibility(View.GONE);
        } else {
            // 공지사항 선택 시
            binding.subScriberTv.setTextColor(ContextCompat.getColor(this, R.color.white));
            binding.subScriberTv.setBackgroundResource(R.drawable.item_bg_on);
            binding.viewTv.setTextColor(ContextCompat.getColor(this, R.color.main));
            binding.viewTv.setBackgroundResource(0);

            // RecyclerView 숨기기
            binding.notificationRecyclerView.setVisibility(View.GONE);
            // 공지사항 RecyclerView 보이기 (예시로 추가)
            binding.subnotificationRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void adjustRecyclerViewWidth(View targetView) {
        ViewTreeObserver viewTreeObserver = targetView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int targetWidth = targetView.getWidth();

                // 타겟 뷰의 너비를 얻어와서 RecyclerView에 적용
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(targetWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
                binding.notificationRecyclerView.setLayoutParams(params);
                binding.subnotificationRecyclerView.setLayoutParams(params);

                // 리스너 제거 (한 번만 실행)
                binding.xmlLinear.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

}