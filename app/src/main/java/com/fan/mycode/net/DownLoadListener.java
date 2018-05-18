package com.fan.mycode.net;

/**
 * author: YangLu
 * create on 2018/5/5 0005
 * description: 进度回调
 */
public interface DownLoadListener extends BaseListener {
    void onProgress(int progress);
}