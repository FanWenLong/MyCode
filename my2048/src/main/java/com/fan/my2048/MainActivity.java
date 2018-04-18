package com.fan.my2048;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by FanWenLong on 2018/4/18.
 * <p>
 * 启动Activity
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainView mv = new MainView(this);
        setContentView(mv);
    }

}
