package com.fan.mycode.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.fan.mycode.BuildConfig;
import com.fan.mycode.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by FanWenLong on 2018/8/8.
 */
public class MyAppWidgetProvider extends AppWidgetProvider {

    public MyAppWidgetProvider() {
        super();
        if (BuildConfig.DEBUG) {
            Log.e(this.getClass().getName(), "MyAppWidgetProvider()");
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (BuildConfig.DEBUG) {
            Log.e(this.getClass().getName(), "MyAppWidgetProvider():onReceive");
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        upDataWidget(context, appWidgetManager, appWidgetIds);
    }

    private void upDataWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            if (BuildConfig.DEBUG) {
                Log.e(this.getClass().getName(), "MyAppWidgetProvider():onUpdate:" + appWidgetIds[i]);
            }
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget);
            rv.setTextViewText(R.id.tv_time, new SimpleDateFormat("hh:mm").format(System.currentTimeMillis()));

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        if (BuildConfig.DEBUG) {
            Log.e(this.getClass().getName(), "MyAppWidgetProvider():onDeleted");
        }
    }
}
