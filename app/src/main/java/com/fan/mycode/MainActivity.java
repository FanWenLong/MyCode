package com.fan.mycode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fan.my2048.MainView;
import com.fan.mycode.net.BaseListener;
import com.fan.mycode.net.OkHttpUtils;
import com.zaaach.citypicker.model.City;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * create by FanWenLong on 2018/4/18
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_one, btn_two, btn_three;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn_one = findViewById(R.id.btn_one);
        btn_two = findViewById(R.id.btn_two);
        btn_three = findViewById(R.id.btn_three);
        btn_one.setOnClickListener(this);
        btn_two.setOnClickListener(this);
        btn_three.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent it = null;
        switch (v.getId()) {
            case R.id.btn_one:
                it = new Intent(MainActivity.this, com.fan.my2048.MainActivity.class);
                startActivity(it);
                break;
            case R.id.btn_two:
                it = new Intent(MainActivity.this, com.yuan2048.MainActivity.class);
                startActivity(it);
                break;
            case R.id.btn_three:
                CityActivity.toMe(MainActivity.this);
                break;
        }
    }
}
