package com.fan.mycode.net;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * author: YangLu
 * create on 2018/5/5 0005
 * description: okhhtp3.5.0版本工具类
 */
public class OkHttpUtils {
    //单例
    private static OkHttpUtils mOkhttpUtils;
    private static OkHttpClient mClient;
    //失败
    private final static int FAILURE = 1001;
    //成功
    private final static int SUCCESS = 1002;
    //进度
    private final static int PROGRESS = 1003;


    static Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == FAILURE) {
                String message = (String) ((Object[]) msg.obj)[1];
                if (message == null) {
                    message = "未知错误";
                } else if (message.contains("Failed to connect to")) {
                    message = "网络未连接，请检查您的网络稍后再试";
                } else if (message.contains("failed to connect to")) {
                    message = "您的网络有问题，请检查您的网络稍后再试";
                } else if (message.contains("UnknownHostException")
                        || message.contains("SocketTimeoutException")
                        || message.contains("time out")) {
                    message = "网络不稳，稍后再试";
                } else if (message.contains("Not Found")) {
                    message = "未找到该界面接口";
                } else if (message.contains("Server Error")) {
                    message = "该界面接口发生异常";
                } else if (message.contains("Unable to resolve host")) {
                    message = "网络异常，请重试";
                } else if (message.contains("未找到用户信息")) {
                }
                ((BaseListener) ((Object[]) msg.obj)[0]).onFailure(message);
                return true;
            } else if (msg.what == SUCCESS) {
                ((BaseListener) ((Object[]) msg.obj)[0]).onResponse((String) ((Object[]) msg.obj)[1]);
                return true;
            } else if (msg.what == PROGRESS) {
                ((DownLoadListener) ((Object[]) msg.obj)[0]).onProgress((int) ((Object[]) msg.obj)[1]);
                return true;
            }
            return false;
        }
    });

    //获取单例
    public static OkHttpUtils getInstance() {
        if (mOkhttpUtils == null) {
            synchronized (OkHttpUtils.class) {
                if (mOkhttpUtils == null) {
                    mOkhttpUtils = new OkHttpUtils();
                }
            }
        }
        return mOkhttpUtils;
    }

    private OkHttpUtils() {
        mClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .cookieJar(new CookieJar() {
                    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url, cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url);
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
//                                .addHeader("user-agent", "Android")
                                .build();
                        return chain.proceed(request);
                    }
                })
                .build();
    }

    /**
     * 设置请求头,会覆盖同名请求头。
     */
    public void setHeaders(final Map<String, String> headers) {
        mClient = mClient.newBuilder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder builder = chain.request().newBuilder();
                        for (String name : headers.keySet()) {
                            builder.header(name, headers.get(name));
                        }
                        Request request = builder.build();
                        return chain.proceed(request);
                    }
                })
                .build();
    }


    /**
     * 普通get请求
     */
    public void get(final String url, final BaseListener mListener) {
        if (!checkNet(mListener)) {
            return;
        }
        //创建一个Request
        final Request request = new Request.Builder()
                .url(url)
                .build();
        //new call
        Call call = mClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                String str = e.getMessage();
                if (str == null) {
                    str = "发生未知错误，请重试";
                }
                handler.obtainMessage(FAILURE, 0, 0, new Object[]{mListener, str}).sendToTarget();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();

                    handler.obtainMessage(SUCCESS, 0, 0, new Object[]{mListener, str}).sendToTarget();
                } else {
                    handler.obtainMessage(FAILURE, 0, 0, new Object[]{mListener, response.message()}).sendToTarget();
                }
            }
        });
    }

    /**
     * 普通的post请求
     */
    public void post(final String url, final Map<String, String> params, final BaseListener mListener) {
        if (!checkNet(mListener)) {
            return;
        }

        FormBody.Builder builder = new FormBody.Builder();

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }

        RequestBody requestBodyPost = builder.build();
        //创建一个Request
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBodyPost)
                .build();
        //new call
        Call call = mClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String str = e.getMessage();
                if (str == null) {
                    str = "发生未知错误，请重试";
                }
                handler.obtainMessage(FAILURE, 0, 0, new Object[]{mListener, str}).sendToTarget();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();

                    handler.obtainMessage(SUCCESS, 0, 0, new Object[]{mListener, str}).sendToTarget();
                } else {

                    handler.obtainMessage(FAILURE, 0, 0, new Object[]{mListener, response.message()}).sendToTarget();
                }
            }
        });
    }

    /**
     * 下载文件，并保存到给定的file中,
     * 第三个参数传递DownLoadListener可以接收进度
     */
    public void donwLoad(final File file, String url, final BaseListener mListener) {
        final Request request = new Request.Builder().url(url).build();
        //new call
        Call call = mClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.obtainMessage(FAILURE, 0, 0, new Object[]{mListener, e.toString()}).sendToTarget();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        handler.obtainMessage(PROGRESS, 0, 0, new Object[]{mListener, progress}).sendToTarget();
                    }
                    fos.flush();
                    handler.obtainMessage(SUCCESS, 0, 0, new Object[]{mListener, "下载成功"}).sendToTarget();
                } catch (Exception e) {
                    handler.obtainMessage(FAILURE, 0, 0, new Object[]{mListener, "下载失败，" + e.toString()}).sendToTarget();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    /**
     * 上传图片，并且可以传递参数
     * <p>
     * uploadFileName,上传图片的参数名，与服务器协商
     */
    public static void UploadImg(final String url, final String uploadFileName, final Map<String, String> params, File img, final DownLoadListener mListener) {
        MultipartBody.Builder buidler = new MultipartBody.Builder();
        buidler.setType(MultipartBody.FORM);
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                buidler.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        buidler.addFormDataPart(uploadFileName, img.getName(),
                RequestBody.create(MediaType.parse("image/jpg"), img));
        RequestBody rb = buidler.build();

        OkHttpClient.Builder b = mClient.newBuilder().addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), mListener))
                        .build();
            }
        });
        OkHttpClient client = b.build();
        Request request = new Request.Builder().url(url).post(rb).build();
        //new call
        Call call = client.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.obtainMessage(FAILURE, 0, 0, new Object[]{mListener, e.toString()}).sendToTarget();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();
                    handler.obtainMessage(SUCCESS, 0, 0, new Object[]{mListener, str}).sendToTarget();
                } else {
                    handler.obtainMessage(FAILURE, 0, 0, new Object[]{mListener, response.message()}).sendToTarget();
                }
            }
        });
    }

    /**
     * 上传图片，并且可以传递参数
     * <p> Map<String, File[]> String file[] 对应的参数名
     */
    public static void UploadMoreImg(final String url, final Map<String, String> params, final Map<String, File[]> img, final DownLoadListener mListener) {
        MultipartBody.Builder buidler = new MultipartBody.Builder();
        buidler.setType(MultipartBody.FORM);
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                buidler.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        if (img != null) {
            for (Map.Entry<String, File[]> entry : img.entrySet()) {
                for (int i = 0; i < entry.getValue().length; i++) {
                    buidler.addFormDataPart(entry.getKey(), entry.getValue()[i].getName(),
                            RequestBody.create(MediaType.parse("image/jpg"), entry.getValue()[i]));
                }
            }
        }

        RequestBody rb = buidler.build();

        OkHttpClient.Builder b = mClient.newBuilder().addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), mListener))
                        .build();
            }
        });
        OkHttpClient client = b.build();
        Request request = new Request.Builder().url(url).post(rb).build();
        //new call
        Call call = client.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.obtainMessage(FAILURE, 0, 0, new Object[]{mListener, e.toString()}).sendToTarget();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();
                    handler.obtainMessage(SUCCESS, 0, 0, new Object[]{mListener, str}).sendToTarget();
                } else {
                    handler.obtainMessage(FAILURE, 0, 0, new Object[]{mListener, response.message()}).sendToTarget();
                }
            }
        });
    }

    private static class ProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private final DownLoadListener progressListener;
        private BufferedSource bufferedSource;

        ProgressResponseBody(ResponseBody responseBody, DownLoadListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
//                    progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    int progress = (int) (totalBytesRead * 100 / responseBody.contentLength());
                    handler.obtainMessage(PROGRESS, 0, 0, new Object[]{progressListener, progress}).sendToTarget();
                    return bytesRead;
                }
            };
        }
    }

    //检查网络
    private boolean checkNet(final BaseListener mListener) {
        boolean isConnect = true;
//        if (!CommonMethod.isOpenNet()) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(MyApp.getInstance().curActivity);
//            builder.setTitle("网络提示");
//            builder.setMessage("天天新衣需要在有网络的情况下使用，请先检查网络");
//            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    mListener.onFailure("网络不可用");
//                }
//            });
//            builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Intent it = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
//                    MyApp.getInstance().curActivity.startActivity(it);
//                    dialog.dismiss();
//                    mListener.onFailure("网络不可用");
//                }
//            });
//            builder.create().show();
//        } else {
//            isConnect = true;
//        }
        return isConnect;
    }


}
