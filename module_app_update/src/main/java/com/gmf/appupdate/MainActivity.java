package com.gmf.appupdate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gmf.util.TestUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TestUtil.test();
    }
}
