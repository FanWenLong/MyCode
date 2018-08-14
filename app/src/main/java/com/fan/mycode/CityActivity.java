package com.fan.mycode;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.fan.mycode.utils.CityUtils;
import com.fan.slideutils.SlidingLayout;
import com.fan.slideutils.SlidingLayoutListener;
import com.fan.slideutils.SlidingUtils;
import com.zaaach.citypicker.model.City;

import java.lang.reflect.Method;

public class CityActivity extends BaseActivity {

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
        new SlidingUtils(this, R.id.activity_city).setLeftAnimate(true).setRightAnimate(true).setSlidingLayoutListener(new SlidingLayoutListener() {
            @Override
            public void onLeftSlidingFinish() {
                if (BuildConfig.DEBUG) {
                    Log.e(this.getClass().toString(), "----------------onLeftSlidingFinish");
                }
                finish();
            }

            @Override
            public void onRightSlidingFinish() {
                if (BuildConfig.DEBUG) {
                    Log.e(this.getClass().toString(), "----------------onRightSlidingFinish");
                }
            }

            @Override
            public void onLeftSlidingCancle() {
                super.onLeftSlidingCancle();
                if (BuildConfig.DEBUG) {
                    Log.e(this.getClass().toString(), "----------------onLeftSlidingCancle");
                }
            }

            @Override
            public void onRightSlidingCancle() {
                super.onRightSlidingCancle();
                if (BuildConfig.DEBUG) {
                    Log.e(this.getClass().toString(), "----------------onRightSlidingCancle");
                }
            }
        }).show();
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (BuildConfig.DEBUG) {
            Log.e(this.getLocalClassName(), "onConfigurationChanged:" + newConfig.screenHeightDp);
        }
    }

    private int getStatusBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Status height:" + height);
        return height;
    }

    private int getNavigationBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Navi height:" + height);
        return height;
    }

    //获取是否存在NavigationBar
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }

}
