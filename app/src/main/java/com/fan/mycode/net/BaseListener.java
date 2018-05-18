package com.fan.mycode.net;

/**
 * author: YangLu
 * create on 2018/5/5 0005
 * description: 网络回调
 */
public interface BaseListener {
    void onFailure(String message);
    void onResponse(String message);
}
