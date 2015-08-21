package com.franklin.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.franklin.util.UtilHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Desc: 配置网络监听器
 * Author:Franklin
 * Date:2015/8/18.
 */
public class MyNetworkListener extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if((networkInfo!=null) && ((networkInfo.getType()==ConnectivityManager.TYPE_MOBILE)||
                (networkInfo.getType()==ConnectivityManager.TYPE_WIFI))) {
            //有网络
            new UtilHelper().ShowToast(context, Toast.LENGTH_SHORT,"网络已经打开!");
        } else {
            new UtilHelper().ShowToast(context,Toast.LENGTH_SHORT,"当前没有打开网络，正在为您打开网络...");
            setNetworkStatus(context, connectivityManager, true); //打开网络
            new UtilHelper().ShowToast(context, Toast.LENGTH_SHORT,"已经打开网络!");
        }

    }

    /**
    * Desc：通过反射获得connectivitymanager的setmobilestatus方法 并设置网络状态
    * Author：Franklin
    * Create Time：2015/8/18 23:10
    * Params:
    * Return:
    * Version:
    */
    public void setNetworkStatus(Context context,ConnectivityManager conMgr,boolean status) {
        Class conMgrClass = null; //ConnectivityManager类
        Field iConMgrField = null; //ConnectivityManager类中的字段
        Object iConMgr = null; //IConnectivityManager类的实例
        Class iConMgrClass = null; //IConetivityManager类
        Method setMobileDataEnabledMethod = null; //setMobileDataEnabled方法

        try {
            conMgrClass = Class.forName(conMgr.getClass().getName()); //得到类
            iConMgrField = conMgrClass.getDeclaredField("mService"); //得到ConnectionManager类的mService对象
            iConMgrField.setAccessible(true); //设置该对象可以访问
            iConMgr = iConMgrField.get(conMgr); //得到mService实例化类IConnectivityManager
            iConMgrClass = Class.forName(iConMgr.getClass().getName()); //得到IConnectivityManager类
            setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true); //设置setMobileDataEnabled方法可以访问
            setMobileDataEnabledMethod.invoke(iConMgr,status);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
