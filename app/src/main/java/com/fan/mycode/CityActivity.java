package com.fan.mycode;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import java.util.ArrayList;
import java.util.List;

public class CityActivity extends AppCompatActivity {

    public static void toMe(Context context) {
        Intent it = new Intent(context, CityActivity.class);
        context.startActivity(it);
    }

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://kaiyixin.libokai.cn/mobile/index/city";
                CityUtils.getInstance().show(getSupportFragmentManager(), url, new CityUtils.MyOnPickListener() {
                    @Override
                    public void onchoose(City data) {
                        if (data == null) {
                            return;
                        }
                        String name = data.getName();
                        String id = data.getCode();
                        String parentId = data.getProvince();
                    }
                });

            }
        });
    }
}