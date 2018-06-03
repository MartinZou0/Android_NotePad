package com.sueword.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;


/**
 * Created by junjianliu
 * on 17/3/23
 * email:spyhanfeng@qq.com
 */

//已完成
public class AppWelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        handler.sendEmptyMessageDelayed(0,5000);
    }

    int time = 1;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            toMainInterface();
            super.handleMessage(msg);
        }
    };

    public void toMainInterface() {
        Intent intent=new Intent(AppWelcomeActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void onResume() {
        super.onResume();
    }
    public void onPause() {
        super.onPause();
    }
}
