package com.franklin;

import android.os.Environment;
import android.test.InstrumentationTestCase;

import com.franklin.domain.SiteObject;
import com.franklin.http.JsonParser;
import com.franklin.xml.XmlHelper;

import junit.framework.TestCase;

import java.util.List;

/**
 * Created by Franklin on 2015/8/8.
 */
public class XmlHelperTest extends TestCase {
        public void testXmlHelper() {
        XmlHelper xmlHelper = new XmlHelper();
        boolean flag = xmlHelper.isFileExists();
        System.out.println("**********"+Environment.getExternalStorageDirectory());
        if(flag) {
            List<SiteObject> siteList = xmlHelper.readSiteList();
            for(SiteObject obj : siteList) {
                System.out.println(obj.toString());
            }
        } else {
            List<SiteObject> siteList = new JsonParser().getSiteList();
            xmlHelper.saveSiteList(siteList);
            System.out.println("save the list,now output: ");
            for(SiteObject obj : siteList) {
                System.out.println(obj.toString());
            }
        }
    }
}
