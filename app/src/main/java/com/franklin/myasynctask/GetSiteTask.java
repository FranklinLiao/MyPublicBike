package com.franklin.myasynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.franklin.db.DbUtil;
import com.franklin.domain.SiteObject;
import com.franklin.http.JsonParser;
import com.franklin.xml.XmlHelper;

import java.util.ArrayList;
import java.util.List;


/**
* Class Name：GetSiteTask
* Desc：用于从网络上获得并保存Site
* Author：Franklin
* Create Time：2015/8/15 6:34
* Modifier：TODO
* Modify Time：TODO
* Modify Comment：TODO
* Version:
*/
public class GetSiteTask  extends AsyncTask<Void,Integer,List<SiteObject>> {
    private Context ctx;
    public GetSiteTask(Context ctx) {
        this.ctx = ctx;
    }


    /**
     * Desc：判断当前是否有存放Site信息的xml文件，如果没有就从网上下载该信息；将该信息解析出来
     * Author：Franklin
     * Create Time：2015/8/15 6:37
     * Params: String... params（未使用）
     * Return: List<SiteObject> (在onpostexecute中保存数据）
     * Version: 1.0
     */
    @Override
    protected List<SiteObject> doInBackground(Void... params) {
        XmlHelper xmlHelper = new XmlHelper();
        List<SiteObject> siteObjectList = new ArrayList<>();
        if(xmlHelper.isFileExists()) {
            siteObjectList = xmlHelper.readSiteList();
        } else {
            siteObjectList = new JsonParser().getSiteList();
            boolean saveFlag = xmlHelper.saveSiteList(siteObjectList);
            if(!saveFlag) { //保存不成功
                xmlHelper.delFile();
            }
        }
        return siteObjectList;
    }

    /**
     * Desc：用于将site的数据放入到数据库中
     * Author：Franklin
     * Create Time：2015/8/15 7:41
     * Params: List<SiteObject>
     * Return: void
     * Version: 1.0
     */
    @Override
    protected void onPostExecute(List<SiteObject> siteObjectList) {
        //往数据库中放数据
        DbUtil dbUtil = new DbUtil(ctx);
        //先清除原有数据
        dbUtil.deleteData();
        for(SiteObject siteObject:siteObjectList) {
            dbUtil.insert(siteObject);
        }
        Toast.makeText(ctx,"update site done",Toast.LENGTH_SHORT).show();
    }
}
