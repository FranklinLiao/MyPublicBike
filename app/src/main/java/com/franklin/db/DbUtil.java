package com.franklin.db;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.baidu.mapapi.model.LatLng;
import com.franklin.domain.SiteObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Franklin on 2015/8/12.
 */
public class DbUtil {
    private Context ctx;

    public DbUtil(Context ctx) {
        this.ctx = ctx;
    }

    public void insert(SiteObject siteObject) {
        SQLiteDatabase db = DbHelper.getDb(ctx);
        String insertSql = "insert into site(name,code,address,lon,lat,num,quyu) values(?,?,?," +
                "?,?,?,?)";
        Object[] values=new Object[]{siteObject.getName(),
        siteObject.getCode(),siteObject.getAddr(),siteObject.getLon(),
        siteObject.getLat(),siteObject.getNum(),siteObject.getQuyu()};
        db.execSQL(insertSql,values);
        DbHelper.closeDb();
    }

    public void deleteData() {
        SQLiteDatabase db = DbHelper.getDb(ctx);
        String insertSql = "delete from site";
        db.execSQL(insertSql);
        DbHelper.closeDb();
    }

    public List<SiteObject> queryByLatLon(LatLng myPos,LatLng latLngThrold) {
        List<SiteObject> siteObjectList = new ArrayList<>();
        SQLiteDatabase db = DbHelper.getDb(ctx);
        double lonThrold = latLngThrold.longitude;
        double latThrold = latLngThrold.latitude;
        String querySql = "select  * from site where (lon between ? and ?) and (lat between ? and ?) order by (lon-?)*(lon-?)+(lat-?)*(lat-?) asc limit 0,11";
        //order by (lon-?)*(lon-?)+(lat-?)*(lat-?) asc
        String[] values = new String[]{String.valueOf(myPos.longitude-lonThrold),String.valueOf(myPos.longitude+lonThrold),
                String.valueOf(myPos.latitude-latThrold),String.valueOf(myPos.latitude+latThrold),
                String.valueOf(myPos.longitude),String.valueOf(myPos.longitude),
                String.valueOf(myPos.latitude),String.valueOf(myPos.latitude)};
        Cursor cursor = db.rawQuery(querySql, values);

        while(cursor.moveToNext()) {
                if(cursor.getColumnIndex("name")==-1 || cursor.getColumnIndex("code")==-1 ||
                   cursor.getColumnIndex("address")==-1 || cursor.getColumnIndex("lon")==-1 ||
                   cursor.getColumnIndex("lat")==-1 || cursor.getColumnIndex("num")==-1 ||
                   cursor.getColumnIndex("quyu") ==-1
                  ) {
                    continue;
                }
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String code = cursor.getString(cursor.getColumnIndex("code"));
                String addr = cursor.getString(cursor.getColumnIndex("address"));
                double lon = cursor.getDouble(cursor.getColumnIndex("lon"));
                double lat = cursor.getDouble(cursor.getColumnIndex("lat"));
                int num = cursor.getInt(cursor.getColumnIndex("num"));
                String quyu = cursor.getString(cursor.getColumnIndex("quyu"));
                SiteObject siteObjectTemp = new SiteObject(name,code,addr,lon,lat,num,quyu);
                siteObjectList.add(siteObjectTemp);
        }

        DbHelper.closeDb();
        return siteObjectList;
    }
}
