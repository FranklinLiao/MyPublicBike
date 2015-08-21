package com.franklin.xml;

import android.os.Environment;

import com.franklin.domain.SiteObject;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



/**
 * Created by Franklin on 2015/8/7.
 */
public class XmlHelper {
    private String fileString = Environment.getExternalStorageDirectory()+"/siteList.xml";

    public boolean saveSiteList(List<SiteObject> siteObjectList) {
        boolean flag = false;
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("Sites");  //创建根节点
        for(SiteObject siteObjectTemp:siteObjectList) {
            System.out.println("save obj :"+siteObjectTemp.toString());
            Element site = root.addElement("site");
            //name
            Element name = site.addElement("name");
            name.setText(siteObjectTemp.getName());
            //code
            Element code = site.addElement("code");
            code.setText(siteObjectTemp.getCode());
            //address
            Element address = site.addElement("address");
            address.setText(siteObjectTemp.getAddr());
            //lon
            Element lon = site.addElement("lon");
            lon.setText(siteObjectTemp.getLon()+"");
            //lat
            Element lat = site.addElement("lat");
            lat.setText(siteObjectTemp.getLat()+"");
            //num
            Element num = site.addElement("num");
            num.setText(siteObjectTemp.getNum()+"");
            //quyu
            Element quyu = site.addElement("quyu");
            quyu.setText(siteObjectTemp.getQuyu());
        }
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        try {
            XMLWriter writer = new XMLWriter(new FileWriter(new File(fileString)),format);
            writer.write(document);
            writer.flush();
            writer.close();
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        Element root = .createElement("Sites");
        this.document.appendChild(root);
        for(SiteObject siteObjectTemp:siteObjectList) {
            Element employee = this.document.createElement("site");
            Element name = this.document.createElement("name");
            name.appendChild(this.document.createTextNode(siteObjectTemp.getName()));
            employee.appendChild(name);
            Element code = this.document.createElement("code");
            code.appendChild(this.document.createTextNode(siteObjectTemp.getCode()));
            employee.appendChild(code);
            Element address = this.document.createElement("address");
            address.appendChild(this.document.createTextNode(siteObjectTemp.getAddr()));
            employee.appendChild(address);
            Element lon = this.document.createElement("lon");
            lon.appendChild(this.document.createTextNode(String.valueOf(siteObjectTemp.getLon())));
            employee.appendChild(lon);
            Element lat = this.document.createElement("lat");
            lat.appendChild(this.document.createTextNode(String.valueOf(siteObjectTemp.getLat())));
            employee.appendChild(lat);
            Element num = this.document.createElement("num");
            num.appendChild(this.document.createTextNode(String.valueOf(siteObjectTemp.getNum())));
            employee.appendChild(num);
            Element quyu = this.document.createElement("quyu");
            quyu.appendChild(this.document.createTextNode(siteObjectTemp.getQuyu()));
            employee.appendChild(quyu);
            root.appendChild(employee);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        try {
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            PrintWriter pw = new PrintWriter(new FileOutputStream(fileString));
            StreamResult result = new StreamResult(pw);
            transformer.transform(source, result);
            flag = true;
        } catch (TransformerConfigurationException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (TransformerException e) {
            System.out.println(e.getMessage());
        }
        */
        return flag;
    }

    public List<SiteObject> readSiteList() {
        List<SiteObject> siteObjectList = new ArrayList<>();
        SAXReader reader = new SAXReader();
        reader.setEncoding("utf-8");
        File file = new File(fileString);
        try {
            Document document = reader.read(file);
            Element root = document.getRootElement();
            Iterator iter = root.elementIterator();
            while(iter.hasNext()) {
                Element site = (Element)iter.next();
                String name = site.elementText("name").trim();
                String code = site.elementText("code").trim();
                String addr = site.elementText("address").trim();
                double lon = Double.parseDouble(site.elementText("lon").trim());
                double lat = Double.parseDouble(site.elementText("lat").trim());
                int num = Integer.parseInt(site.elementText("num").trim());
                String quyu = site.elementText("quyu").trim();
                SiteObject siteObject = new SiteObject(name,code,addr,lon,lat,num,quyu);
               // System.out.println(siteObject.toString());
                siteObjectList.add(siteObject);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        /*
        try {
            File file = new File(fileString);
            document = builder.parse(file);
            NodeList list = document.getElementsByTagName("site");
            for(int nodeIndex=0;nodeIndex<list.getLength();nodeIndex++) {
                String name = document.getElementsByTagName("name").item(nodeIndex)
                        .getFirstChild().getNodeValue();
                String code = document.getElementsByTagName("code").item(nodeIndex)
                        .getFirstChild().getNodeValue();
                String addr = document.getElementsByTagName("address").item(nodeIndex)
                        .getFirstChild().getNodeValue();
                if(document.getElementsByTagName("lon").item(nodeIndex).getFirstChild().getNodeValue()==null
                        || document.getElementsByTagName("lat").item(nodeIndex).getFirstChild().getNodeValue()==null
                        || document.getElementsByTagName("num").item(nodeIndex).getFirstChild().getNodeValue()==null ) {
                    continue;
                }
                double lon = Double.parseDouble(document.getElementsByTagName("lon").item(nodeIndex)
                        .getFirstChild().getNodeValue());
                double lat = Double.parseDouble(document.getElementsByTagName("lat").item(nodeIndex)
                        .getFirstChild().getNodeValue());
                int num = Integer.parseInt(document.getElementsByTagName("num").item(nodeIndex)
                        .getFirstChild().getNodeValue());
                String quyu = document.getElementsByTagName("quyu").item(nodeIndex)
                        .getFirstChild().getNodeValue();
                SiteObject siteObject = new SiteObject(name,code,addr,lon,lat,num,quyu);
                System.out.println(siteObject.toString());
                siteObjectList.add(siteObject);
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        return siteObjectList;
    }

    public boolean isFileExists() {
        boolean flag = false;
        File file = new File(fileString);
        if(file.exists()) {
            flag = true;
        }
        return flag;
    }

    public void delFile() {
        File file = new File(fileString);
        if(file.exists()) {
            file.delete();
        }
    }
}
