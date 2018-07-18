package com.fan.mycode;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fan.mycode.utils.CityUtils;
import com.fan.slideutils.SlidingLayout;
import com.fan.slideutils.SlidingLayoutListener;
import com.zaaach.citypicker.model.City;

public class CityActivity extends AppCompatActivity {

    public static void toMe(Context context) {
        Intent it = new Intent(context, CityActivity.class);
        context.startActivity(it);
    }

    Button button;
    TextView textView;
    SlidingLayout sliding_city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        sliding_city = findViewById(R.id.sliding_city);
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
//        sliding_city.setSlidingLayoutListener(new SlidingLayoutListener() {
//            @Override
//            public void onLeftSlidingFinish() {
//                if (BuildConfig.DEBUG) {
//                    Log.e(this.getClass().toString(), "----------------onLeftSlidingFinish");
//                }
//            }
//
//            @Override
//            public void onRightSlidingFinish() {
//                if (BuildConfig.DEBUG) {
//                    Log.e(this.getClass().toString(), "----------------onRightSlidingFinish");
//                }
//            }
//
//            @Override
//            public void onLeftSliding() {
//                super.onLeftSliding();
//                if (BuildConfig.DEBUG) {
//                    Log.e(this.getClass().toString(), "----------------onLeftSliding");
//                }
//            }
//
//            @Override
//            public void onLeftSlidingCancle() {
//                super.onLeftSlidingCancle();
//                if (BuildConfig.DEBUG) {
//                    Log.e(this.getClass().toString(), "----------------onLeftSlidingCancle");
//                }
//            }
//
//            @Override
//            public void onRightSliding() {
//                super.onRightSliding();
//                if (BuildConfig.DEBUG) {
//                    Log.e(this.getClass().toString(), "----------------onRightSliding");
//                }
//            }
//
//            @Override
//            public void onRightSlidingCancle() {
//                super.onRightSlidingCancle();
//                if (BuildConfig.DEBUG) {
//                    Log.e(this.getClass().toString(), "----------------onRightSlidingCancle");
//                }
//            }
//        });
    }
}
