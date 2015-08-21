package com.franklin.util;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.franklin.mybike.R;

/**
* Class Name：UtilHelper
* Desc： 辅助类
* Author：Franklin
* Create Time：2015/8/14 6:47
* Modifier：TODO
* Modify Time：TODO
* Modify Comment：TODO
* Version：1.0
*/
public class UtilHelper {
    private final double PI = 3.1415926;
    private final double RADIUS = 6372.797; //km 地球半径

    /**
    * Desc：将范围转化为所在对应的经纬度偏差，用于数据库选择部分Site
    * Author：Franklin
    * Create Time：2015/8/14 6:51
    * Params: double,LatLng   double:范围（m)   LatLng:所在点的坐标
    * Return: LatLng:所在点的对应范围内经纬度偏差
    * Version: 1.0
    */
    public LatLng transDistanceToLonLat(LatLng myPos,double distance) {
        double lngPerKiloMeter = 180.0 / PI * (distance/1000.0) / RADIUS; //千米对应的经度值
        double latPerKiloMter = lngPerKiloMeter / Math.cos(myPos.latitude*PI/180);//千米对应的纬度值
        LatLng latLng = new LatLng(latPerKiloMter,lngPerKiloMeter);
        return latLng;
    }

    public void ShowToast(Context context,int lastTime,String message)
    {
        View toastRoot = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.mytoast, null);
        TextView view = (TextView) toastRoot.findViewById(R.id.message);
        view.setBackgroundColor(Color.WHITE);
        view.setText(message);
        view.setTextColor(Color.BLACK);
        Toast toastStart = new Toast(context);
        toastStart.setGravity(Gravity.BOTTOM, 0, 30);
        toastStart.setDuration(lastTime);
        toastStart.setView(toastRoot);
        toastStart.show();
    }

}
