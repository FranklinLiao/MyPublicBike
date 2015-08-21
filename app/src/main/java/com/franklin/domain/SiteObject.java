package com.franklin.domain;



public class SiteObject {
	private String name = "";
	private String code = "";
	private String addr = "";
	private double lon = 0;
	private double lat = 0;
	private int num = 0;
	private String quyu = "";
	
	public SiteObject(String name,String code,String addr,
			double lon,double lat,int num,String quyu) {
		this.name = name;
		this.code = code;
		this.addr = addr;
		this.lon = lon;
		this.lat = lat;
		this.num = num;
		this.quyu = quyu;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getQuyu() {
		return quyu;
	}
	public void setQuyu(String quyu) {
		this.quyu = quyu;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
}
