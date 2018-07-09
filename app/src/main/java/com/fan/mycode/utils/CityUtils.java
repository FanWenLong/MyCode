package com.fan.mycode.utils;

import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.fan.mycode.R;
import com.fan.mycode.net.BaseListener;
import com.fan.mycode.net.OkHttpUtils;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.DataSource;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FanWenLong on 2018/5/18.
 */
public class CityUtils {
    private static CityUtils cityUtils;
    LocatedCity mLocatedCity = new LocatedCity("定位中...", "", "");
    List<HotCity> hotCities = new ArrayList<>();
    List<City> citiesList = new ArrayList<>();

    private String url;

    private CityUtils() {
    }

    public interface MyOnPickListener {
        void onchoose(City data);
    }

    public static CityUtils getInstance() {
        if (cityUtils == null) {
            synchronized (CityUtils.class) {
                cityUtils = new CityUtils();
            }
        }
        return cityUtils;
    }

    public void show(FragmentManager fm, String url, MyOnPickListener mOnPickListener) {
        //1.获取数据
//        getData(url, fm, mOnPickListener);
//        //2.显示控件
//        showDialog(fm, mOnPickListener);
//        //3.调用定位
//        locationCity();
        getData(url, fm, mOnPickListener);
    }

    /**
     * 开始定位
     */
    public void locationCity() {
        //成功后调用refreshLocation方法
        //失败后调用 CityPicker.getInstance().locateComplete(mLocatedCity, LocateState.FAILURE);
        CityPicker.getInstance().locateComplete(mLocatedCity, LocateState.LOCATING);//显示定位中

        //模拟定位
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //定位完成之后更新数据
                CityPicker.getInstance()
                        .locateComplete(new LocatedCity("深圳", "广东", "101280601"), LocateState.SUCCESS);
            }
        }, 2000);

    }

    /**
     * 刷新定位
     */
    public void refreshLocation(LocatedCity city) {
        CityPicker.getInstance().locateComplete(city, LocateState.SUCCESS);
    }

    /**
     * 获取数据
     */
    private void getData(String url, final FragmentManager fm, final MyOnPickListener mOnPickListener) {
        if (citiesList != null && citiesList.size() > 0) {
            showDialog(fm, mOnPickListener);
            return;
        }

        if (url == null || url.equals("")) {
            try {
                initData(DataSource.Jsondata, fm, mOnPickListener);
            } catch (JSONException e) {
                Log.e("1", e.toString());
            }
            return;
        }

        OkHttpUtils.getInstance().get(url, new BaseListener() {
            @Override
            public void onFailure(String message) {
                showDialog(fm, mOnPickListener);
            }

            @Override
            public void onResponse(String message) {
                try {
                    initData(message, fm, mOnPickListener);
                } catch (JSONException e) {
                    Log.e("1", e.toString());
                }
            }
        });
    }

    /**
     * 解析数据
     */
    private void initData(String message, FragmentManager fm, MyOnPickListener mOnPickListener) throws JSONException {
        JSONObject json = new JSONObject(message);
        if (json.getInt("status") == 1) {
            JSONObject data = json.getJSONObject("data");
            //解析热门城市
            JSONArray hot = data.getJSONArray("hot");
            for (int i = 0; i < hot.length(); i++) {
                JSONObject object = hot.getJSONObject(i);
                String name = object.getString("name");
                String parentId = object.getString("parent_id");
                String id = object.getString("id");
                HotCity hotCity = new HotCity(name, parentId, id);
                hotCities.add(hotCity);
            }
            //解析所有城市
            JSONArray all = data.getJSONArray("all");
            for (int i = 0; i < all.length(); i++) {
                JSONObject object = all.getJSONObject(i);
                JSONArray cityJson = object.getJSONArray("city");
                for (int j = 0; j < cityJson.length(); j++) {
                    JSONObject jo = cityJson.getJSONObject(j);
                    String name = jo.getString("name");
                    String parentId = jo.getString("parent_id");
                    String id = jo.getString("id");
                    String pinyin = jo.getString("pinyin");
                    City city = new City(name, parentId, pinyin, id);
                    citiesList.add(city);
                }
            }
        }
        showDialog(fm, mOnPickListener);
    }

    /**
     * 显示界面
     */
    private void showDialog(FragmentManager fm, final MyOnPickListener mOnPickListener) {
        CityPicker.getInstance()
                .setFragmentManager(fm)    //此方法必须调用
                .enableAnimation(true)    //启用动画效果
                .setAnimationStyle(R.style.DefaultCityPickerAnimation)    //自定义动画
                .setLocatedCity(mLocatedCity)  //APP自身已定位的城市，默认为null（定位失败）
                .setHotCities(hotCities)//指定热门城市
                .setCityList(citiesList)//指定所有城市,此处如果不指定，则代表使用默认的数据，即本地数据库
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        mOnPickListener.onchoose(data);
                    }

                    @Override
                    public void onLocate() {
                        locationCity();
                    }
                })
                .show();
        //定位
        locationCity();
    }

}
