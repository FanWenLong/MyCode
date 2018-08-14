package com.fan.mycode;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import com.fan.mycode.adapter.MainAdapter;


/**
 * create by FanWenLong on 2018/4/18
 */
public class MainActivity extends BaseActivity {

    RecyclerView recycler_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        recycler_main = findViewById(R.id.recycler_main);
        recycler_main.setLayoutManager(new LinearLayoutManager(this));
        recycler_main.setAdapter(new MainAdapter(this));
    }
}
