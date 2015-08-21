package com.franklin.mybike;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;

import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.franklin.listener.MyNetworkListener;
import com.franklin.myasynctask.GetSiteTask;
import com.franklin.db.DbUtil;
import com.franklin.domain.SiteObject;
import com.franklin.listener.MyGpsListener;
import com.franklin.util.UtilHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    public MapView mMapView;
    public BaiduMap mMap;
    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    public LatLng myPos;
    public Marker myposMarker;
    public Marker siteMarker;
    public MyNetworkListener myNetworkListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //打开GPS
        MyGpsListener myGpsListener = new MyGpsListener(MainActivity.this);
        myGpsListener.getMyGpsOpen();
        //注册网络的监听器 androidmanifest中配置了广播的监听器 不用代码中再配置
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        myNetworkListener = new MyNetworkListener();
        registerReceiver(myNetworkListener, intentFilter);
        //初始化
        BaiduMapOptions mapOptions = new BaiduMapOptions();
        mapOptions.compassEnabled(true); //开启罗盘
        mapOptions.rotateGesturesEnabled(true); //允许旋转
        mMapView = (MapView) findViewById(R.id.bmapView);
        //mMapView = new MapView(MainActivity.this,mapOptions);
        mMap = mMapView.getMap();

        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        initLocation(); //定位参数设定
        mLocationClient.start(); //启动定位
        //---------------------------Button------------------
        //我的位置
        Button myposButton = (Button) findViewById(R.id.mypos);
        //更新服务点位置
        Button updateSitesButton = (Button) findViewById(R.id.updateSites);
        //显示附近的服务点
        Button showSitesButton =(Button) findViewById(R.id.showSites);
        //-----------------------事件监听----------------------
        //覆盖物点击
        BaiduMap.OnMarkerClickListener listener = new BaiduMap.OnMarkerClickListener() {
            /**
             * 地图 Marker 覆盖物点击事件监听函数
             * @param marker 被点击的 marker
             */
            public boolean onMarkerClick(Marker marker){
                String title = marker.getTitle();
                if(title!=null) {
                    new UtilHelper().ShowToast(MainActivity.this, Toast.LENGTH_LONG, title);
                }
                return true;
            }
        };
        mMap.setOnMarkerClickListener(listener);
        //点击我的位置
        myposButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myPos != null) {
                    mMap.setMapStatus(MapStatusUpdateFactory.newLatLng(myPos));
                } else {
                    new UtilHelper().ShowToast(MainActivity.this,Toast.LENGTH_SHORT,"您的当前位置尚未确定，请稍后再试!");
                }
            }
        });
        //点击更新服务点
        updateSitesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              GetSiteTask task = new GetSiteTask(MainActivity.this);
              task.execute();
//            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//            builder.setTitle("更新服务点提示");
//            builder.setMessage("您是否要更新服务点？");
//            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    GetSiteTask task = new GetSiteTask(null);
//                    task.execute();
//                }
//            });
//            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    //取消就不更新
//                }
//            });
//            builder.create().show();
            }
        });
        //点击显示周围服务点
        showSitesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double distance = 5000;
                showSites(distance);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mMapView.onPause();
    }

//    @Override
//    public void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        if(myNetworkListener!=null) {
//            unregisterReceiver(myNetworkListener);
//        }
////        if(mViewFlipper != null){
////            mViewFlipper .onDetachedFromWindow();
////        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(false);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }
    /**
     * 实现实时位置回调监听
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            //移动地图的中心点
            //定义Maker坐标点
            LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_mypos);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            myPos = point;
            if(myposMarker!=null) {
                myposMarker.remove();
            }
            myposMarker = (Marker) mMap.addOverlay(option);
            //mMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
        }
    }





    public void showSites(double distance) {
        List<SiteObject> siteObjectList = new ArrayList<>();
        if(myPos==null) {
            new UtilHelper().ShowToast(MainActivity.this,Toast.LENGTH_SHORT,"当前位置尚未确定，请稍后再试!");
        }
        DbUtil dbUtil = new DbUtil(this);
        LatLng latLngThrold = new UtilHelper().transDistanceToLonLat(myPos,distance);
        siteObjectList = dbUtil.queryByLatLon(myPos,latLngThrold);
        mMap.clear(); //清除所有覆盖物
        for(SiteObject siteObject:siteObjectList) {
            LatLng point = new LatLng(siteObject.getLat(), siteObject.getLon());
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_site);
            //构建MarkerOption，用于在地图上添加Marker
            // siteMarker.setTitle(siteObject.getName());
            String title = siteObject.getAddr();
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap)
                    .title(title);
            //在地图上添加Marker，并显示
            Marker siteMarker = (Marker) mMap.addOverlay(option);
            mMap.addOverlay(option);
        }
    }
}
