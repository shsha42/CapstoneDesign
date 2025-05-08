package com.codebly.zibro.view.home.menu.friends;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.codebly.zibro.R;

public class FriendPageActivity extends AppCompatActivity {
ImageView imageView27, imageView26;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_friend_page);

        //친구 검색
        imageView27 = (ImageView)findViewById(R.id.imageView27);
        imageView27.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SearchFriendActivity.class);
            startActivity(intent);
        });
        //친구 편집
        imageView26 = (ImageView)findViewById(R.id.imageView26);
        imageView26.setOnClickListener(view1 -> {
            Intent intent1 = new Intent(getApplicationContext(), AlterFriendsActivity.class);
            startActivity(intent1);
        });

        //친구 추가는 QR코드이라서 나중에 작성
        /*
        * 코드는 여기에
        * */

        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}