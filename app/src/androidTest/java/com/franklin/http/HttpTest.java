package com.franklin.http;

import com.franklin.domain.SiteObject;

import junit.framework.TestCase;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Franklin on 2015/8/2.
 */
public class HttpTest  extends TestCase{
    public void testGetHtml() throws Exception {
        String urlString = "http://www.bjjtw.gov.cn/bjggzxc/ggfw_PointQueryAction_dbList.action?";
        String htmlPage = new GetHtml().getHtmlPage();
        System.out.println(htmlPage);
    }

    public void testJsonParser() throws Exception {
        String urlString = "http://www.bjjtw.gov.cn/bjggzxc/ggfw_PointQueryAction_dbList.action?";
        List<SiteObject> list = new JsonParser().getSiteList();
        Iterator<SiteObject> iterator = list.iterator();
        while(iterator.hasNext()) {
            System.out.println(iterator.next().toString());
        }
    }
}
