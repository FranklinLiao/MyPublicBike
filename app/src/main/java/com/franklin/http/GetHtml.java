package com.franklin.http;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.client.methods.HttpGet;

public class GetHtml {
	private String urlString = "http://www.bjjtw.gov.cn/bjggzxc/ggfw_PointQueryAction_dbList.action?";
	public String getHtmlPage() {
		String htmlPageString = "";
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(urlString);
		int code = 0;
		try {
			code = httpClient.executeMethod( getMethod);
			if(code==HttpStatus.SC_OK) {
				htmlPageString =  getMethod.getResponseBodyAsString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			 getMethod.releaseConnection();
			return htmlPageString;
		}
	}
}
