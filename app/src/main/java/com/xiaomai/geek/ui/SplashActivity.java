package com.xiaomai.geek.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by XiaoMai on 2017/4/13.
 */

public class SplashActivity extends AppCompatActivity {

    private String TAG = "SplashActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"zxczxczxc");
        MainActivity.launch(this);
        finish();
    }
}
