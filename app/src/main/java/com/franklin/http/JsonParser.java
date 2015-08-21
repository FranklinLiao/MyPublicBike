package com.franklin.http;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.franklin.domain.SiteObject;

public class JsonParser {
	public List<SiteObject> getSiteList() {
		List<SiteObject> siteLists = new ArrayList<SiteObject>();
		String siteInfos = new GetHtml().getHtmlPage();
		try {
			JSONArray jsonArray = new JSONArray(siteInfos);
			for(int index=0;index<jsonArray.length();index++) {
				JSONObject jsonObject = jsonArray.getJSONObject(index);
				String name = getStringInfo(jsonObject,"name");
				String code = getStringInfo(jsonObject,"code");
				String addr = getStringInfo(jsonObject,"addr");
				double lon = getDoubleInfo(jsonObject,"lon");
				double lat = getDoubleInfo(jsonObject,"lat");
				int num = getIntInfo(jsonObject,"num");
				String quyu = getStringInfo(jsonObject,"quyu");
				SiteObject siteObject = new SiteObject(name, code,addr, lon, lat, num, quyu);
				//System.out.println(name);
				siteLists.add(siteObject);	
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			return siteLists;
		}
	}
	
	public String getStringInfo(JSONObject object,String name) throws Exception{
		if(!object.has(name)) {
			return "";
		}
		return object.getString(name);
	}
	
	public static double getDoubleInfo(JSONObject object,String name) throws Exception{
		if(!object.has(name)) {
			return 0;
		}
		return object.getDouble(name);
	}
	
	public static int getIntInfo(JSONObject object,String name) throws Exception{
		if(!object.has(name)) {
			return 0;
		}
		return object.getInt(name);
	}
}
