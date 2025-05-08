package com.codebly.zibro.view.home.general;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.codebly.zibro.R;
import com.codebly.zibro.view.home.HomeActivity;

public class GeneralHomeLocationActivity extends AppCompatActivity {
ImageView imageView37;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageView37 = (ImageView) findViewById(R.id.imageView37);
        imageView37.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GeneralHomeLocationActivity.this, HomeActivity.class));
            }
        });
        setContentView(R.layout.fragment_general_home_location);
        //코드 구현 중단- 용만이가 시킴
    }
}