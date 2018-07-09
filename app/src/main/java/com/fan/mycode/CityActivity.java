package com.fan.mycode;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fan.mycode.utils.CityUtils;
import com.zaaach.citypicker.model.City;

public class CityActivity extends AppCompatActivity {

    public static void toMe(Context context) {
        Intent it = new Intent(context, CityActivity.class);
        context.startActivity(it);
    }

    Button button;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "";
                CityUtils.getInstance().show(getSupportFragmentManager(), url, new CityUtils.MyOnPickListener() {
                    @Override
                    public void onchoose(City data) {
                        if (data == null) {
                            textView.setText("取消选择");
                            return;
                        }
                        String name = data.getName();
                        String id = data.getCode();
                        String parentId = data.getProvince();
                        textView.setText(name + id + parentId);
                    }
                });

            }
        });
    }
}
