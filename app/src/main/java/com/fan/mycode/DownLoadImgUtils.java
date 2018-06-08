package com.fan.mycode;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.fan.mycode.net.BaseListener;
import com.fan.mycode.net.OkHttpUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by FanWenLong on 2018/6/7.
 * <p>
 * //这个imgList是你网络图片的列表，请替换
 * List<String> listImg = new ArrayList<>();
 * listImg.add("https://bpic.588ku.com/original_origin_min_pic/18/05/09/48473ba8e43078233e6c2b5fde59297f.jpg!r650");
 * listImg.add("https://bpic.588ku.com/original_origin_min_pic/18/05/20/57e72d7c84abc5ebadac1c162a7624b6.jpg!r650");
 * <p>
 * //这个imgList是你网络图片的列表，请替换
 * List<String> listImg2 = new ArrayList<>();
 * listImg2.add("http://img.zcool.cn/community/010f87596f13e6a8012193a363df45.jpg@1280w_1l_2o_100sh.jpg");
 * listImg2.add("http://img.zcool.cn/community/01f09e577b85450000012e7e182cf0.jpg@1280w_1l_2o_100sh.jpg");
 * <p>
 * //初始化工具
 * DownLoadImgUtils downLoadImgUtils = new DownLoadImgUtils(MainActivity.this);
 * //添加任务
 * downLoadImgUtils.add(listImg, new DownLoadImgUtils.onFinishListener() {
 *
 * @Override public void success(List<String> imgList, List<File> files) {
 * //所有图片下载成功了，这个imgList参数是下载好的图片的地址，可直接给adapter使用
 * Log.e("1", "拿到1 " + imgList.get(0));
 * Log.e("1", "拿到1 " + files.get(0).getPath());
 * Log.e("1", "拿到2 " + imgList.get(1));
 * Log.e("1", "拿到2 " + files.get(1).getPath());
 * }
 * @Override public void error(String message) {
 * Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
 * }
 * });
 * <p>
 * downLoadImgUtils.add(listImg2, new DownLoadImgUtils.onFinishListener() {
 * @Override public void success(List<String> imgList, List<File> files) {
 * Log.e("1", "拿到1 " + imgList.get(0));
 * Log.e("1", "拿到1 " + files.get(0).getPath());
 * Log.e("1", "拿到2 " + imgList.get(1));
 * Log.e("1", "拿到2 " + files.get(1).getPath());
 * }
 * @Override public void error(String message) {
 * Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
 * }
 * });
 * <p>
 */
public class DownLoadImgUtils {
    /**
     * 下载图片
     */
    private final int DOWNLOAD_IMG = 1;
    /**
     * 成功
     */
    private final int RESULT_SUCCESS = 2;
    /**
     * 失败
     */
    private final int RESULT_FAILD = 3;

    private Context context;
    /**
     * 线程池
     */
    private ExecutorService executorService = null;
    /**
     * 任务队列
     */
    private LinkedList list = new LinkedList();

    /**
     * 下载回调
     */
    public interface onFinishListener {
        /**
         * @param imgList 本地图片路径列表
         * @param files   本地图片文件列表
         */
        void success(List<String> imgList, List<File> files);

        /**
         * @param message 错误信息
         */
        void error(String message);
    }

    public DownLoadImgUtils(Context context) {
        this.context = context;
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * 添加需要下载的图片列表
     *
     * @param url      要下载的图片列表
     * @param listener 下载完成的回调
     */
    public void add(final List<String> url, final onFinishListener listener) {
        if (url == null || url.size() == 0 || listener == null) {
            listener.error("图片列表或回调监听不能为空");
            return;
        }
        //获取用于保存图片的文件列表
        final List<File> files = getFiles(url.size());
        //需要执行的线程列表
        List<MyTask> data = new ArrayList<>();
        for (int i = 0; i < url.size(); i++) {
            //创建下载第i个图片的线程
            MyTask myTaskTemp = new MyTask(i, files.get(i), url.get(i), new CallBack() {
                @Override
                public void success(int index) {
                    //将index位置上的图片网络地址替换为下载好的图片的文件路径
                    url.set(index, files.get(index).getPath());
                    if (index == url.size() - 1) {
                        //列表所有图片下载完成，进行回调
                        listener.success(url, files);
                    }
                }

                @Override
                public void error(int index) {
                    listener.error("第 " + index + " 张  下载失败");
                }
            });
            //保存该线程
            data.add(myTaskTemp);
        }
        //将保存的线程列表添加到队列中
        list.addAll(data);
        //开始执行
        execute();
    }

    /**
     * 线程池执行任务队列中的线程
     */
    private void execute() {
        while (!list.isEmpty()) {
            executorService.execute((MyTask) list.removeFirst());
        }
    }

    /**
     * 获取对应数量的文件用于保存下载的图片
     *
     * @param size 需要的数量
     * @return 文件的列表
     */
    private List<File> getFiles(int size) {
        List<File> files = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            files.add(getFile(i));
        }
        return files;
    }

    /**
     * 获取一个图片文件
     *
     * @param i 理论上可以避免获取到同名的文件。
     * @return 一个图片
     */
    private File getFile(int i) {
        String path = context.getExternalCacheDir().getPath() + "/download/";
        File savedir = new File(path);
        if (!savedir.exists()) {
            savedir.mkdirs();
        }
        String fileName = "download" + System.currentTimeMillis() + "_" + i + ".jpg";

        return new File(path + fileName);
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.arg1 == DOWNLOAD_IMG) {
                Object[] objects = (Object[]) msg.obj;
                if (msg.arg2 == RESULT_SUCCESS) {
                    ((CallBack) objects[1]).success((int) objects[0]);
                } else if (msg.arg2 == RESULT_FAILD) {
                    ((CallBack) objects[1]).error((int) objects[0]);
                }
                return true;
            }
            return false;
        }
    });

    /**
     * 线程执行结果回调
     */
    public interface CallBack {
        /**
         * @param index 当前线程的位置
         */
        void success(int index);

        /**
         * @param index 当前线程的位置
         */
        void error(int index);
    }

    /**
     * 下载图片的线程
     */
    class MyTask implements Runnable {

        File file = null;
        String url = "";
        CallBack listener;
        int index;//记录第几个

        /**
         * @param index    当前线程的位置
         * @param file     保存图片的文件
         * @param url      图片的下载地址
         * @param listener 线程执行结果回调
         */
        public MyTask(int index, File file, String url, CallBack listener) {
            this.index = index;
            this.file = file;
            this.url = url;
            this.listener = listener;
        }

        @Override
        public void run() {
            try {
                URL temp = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) temp.openConnection();
                conn.setConnectTimeout(5 * 1000);

                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                //获取网络返回流
                InputStream is = conn.getInputStream();
                //打开文件写入流
                fos = new FileOutputStream(file);
                //循环写入文件
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                //刷新写入流缓冲
                fos.flush();
                //关闭流
                try {
                    if (is != null)
                        is.close();
                    if (fos != null)
                        fos.close();
                } catch (IOException e) {
                    Message msg = handler.obtainMessage();
                    msg.arg1 = DOWNLOAD_IMG;
                    msg.arg2 = RESULT_FAILD;
                    msg.obj = new Object[]{index, listener, "error:" + e.toString()};
                    handler.sendMessage(msg);
                    return;
                }
                Message msg = handler.obtainMessage();
                msg.arg1 = DOWNLOAD_IMG;
                msg.arg2 = RESULT_SUCCESS;
                msg.obj = new Object[]{index, listener, "success"};
                handler.sendMessage(msg);

            } catch (MalformedURLException e) {
                Message msg = handler.obtainMessage();
                msg.arg1 = DOWNLOAD_IMG;
                msg.arg2 = RESULT_FAILD;
                msg.obj = new Object[]{index, listener, "error:" + e.toString()};
                handler.sendMessage(msg);
            } catch (IOException e) {
                Message msg = handler.obtainMessage();
                msg.arg1 = DOWNLOAD_IMG;
                msg.arg2 = RESULT_FAILD;
                msg.obj = new Object[]{index, listener, "error:" + e.toString()};
                handler.sendMessage(msg);
            }
        }
    }
}
