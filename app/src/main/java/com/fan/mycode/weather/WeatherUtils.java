package com.fan.mycode.weather;

import com.fan.mycode.net.BaseListener;
import com.fan.mycode.net.OkHttpUtils;

/**
 * Created by FanWenLong on 2018/8/8.
 * <p>
 * 获取天气
 */
public class WeatherUtils {

    private String html;

    public WeatherUtils() {
        getData();
    }

    private void getData() {
        String url = "http://m.weathercn.com/zh/cn/chaoyang-district/57456/current-weather/57456?lang=zh-cn?partner=huawei13";
        OkHttpUtils.getInstance().get(url, new BaseListener() {
            @Override
            public void onFailure(String message) {

            }

            @Override
            public void onResponse(String message) {
                html = message;
            }
        });
    }

}
