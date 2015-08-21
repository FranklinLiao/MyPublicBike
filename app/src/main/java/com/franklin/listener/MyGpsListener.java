package com.franklin.listener;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.widget.Toast;

import com.franklin.util.UtilHelper;

/**
 * Desc: 检查GPS是否打开 如果没有打开，那么打开GPS
 * Author:Franklin
 * Date:2015/8/19
 */

public class MyGpsListener {
    private Context ctx;
    public MyGpsListener(Context ctx) {
        this.ctx = ctx;
    }

    public void getMyGpsOpen() {
        if(checkGpsStatus()) {
            new UtilHelper().ShowToast(ctx, Toast.LENGTH_SHORT,"GPS已打开!");
        } else {
            new UtilHelper().ShowToast(ctx, Toast.LENGTH_SHORT,"GPS未打开，正在打开GPS!");
            openGps();
            new UtilHelper().ShowToast(ctx, Toast.LENGTH_SHORT, "已经将GPS打开!");
        }
    }

    /**
    * Desc：检测GPS或者AGPS是否打开
    * Author：Franklin
    * Create Time：2015/8/19 20:16
    * Params:
    * Return: true:打开 false:未打开
    * Version:
    */
    public boolean checkGpsStatus() {
        boolean flag = false;
        LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsFlag = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean agpsFlag = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        flag = gpsFlag || agpsFlag;
        return flag;
    }

    /**
    * Desc：采用反射  打开Gps设置，并自动设置为打开  3表示gps
    * Author：Franklin
    * Create Time：2015/8/19 20:22
    * Params:
    * Return:
    * Version:
    */
    public void openGps() {
        Intent gpsIntent = new Intent();
        gpsIntent.setClassName("com.android.settings","com.android.settings.widget.SettingAppWidgetProvider");
        gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
        gpsIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(ctx,0,gpsIntent,0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
}
