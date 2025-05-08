package com.codebly.zibro.view.common;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.codebly.zibro.R;

public class Toolbar extends AppCompatActivity {
    /*
    객체는 상속이 아니지.
    Toolbar tlbr = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(tlbr);
     */
    ActionBar actionBar = getSupportActionBar();
    //actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기버튼
}
