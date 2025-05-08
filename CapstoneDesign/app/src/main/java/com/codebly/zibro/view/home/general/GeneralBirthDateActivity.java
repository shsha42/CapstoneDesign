package com.codebly.zibro.view.home.general;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.codebly.zibro.R;

public class GeneralBirthDateActivity extends AppCompatActivity {
ImageView imageView6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_general_birth_date);
        imageView6 = (ImageView) findViewById(R.id.imageView6);
        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GeneralBirthDateActivity.this, GeneralGenderActivity.class));
            }//용만이가 중단시킴
        });
    }
}


