package com.codebly.zibro.view.home.general;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.codebly.zibro.R;

public class GeneralGenderActivity extends AppCompatActivity {
ImageView imageView12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_general_gender);
        imageView12 = (ImageView) findViewById(R.id.imageView12);
        imageView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GeneralGenderActivity.this, GeneralHomeLocationActivity.class));
            }//용만이가 중단 시킴
        });
    }
}