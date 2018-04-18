package com.fan.mycode;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fan.my2048.MainView;


/**
 * create by FanWenLong on 2018/4/18
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_one;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        tv_one = findViewById(R.id.tv_one);
        tv_one.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_one:
                Intent it = new Intent(MainActivity.this, com.fan.my2048.MainActivity.class);
                startActivity(it);
                break;
        }
    }
}
