package com.thingtek.config;

import com.thingtek.beanServiceDao.base.service.BaseService;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/*
    端口参数
 */
@Component
public class PortConfig extends BaseService {
    private String iecip;
    private int port;
    private int iecport;
    private int baudrate;

    public int getBaudrate() {
        return baudrate;
    }

    public String getIecip() {
        return iecip;
    }

    public int getPort() {
        return port;
    }

    public int getIecport() {
        return iecport;
    }

    private String filename = "NetConfig.xml";

    public PortConfig() {
        init();
    }

    private void init() {
        File configfile = new File(filename);
        if (!configfile.exists()) {
            initDefault();
            createConfigXML();
        } else {
            try {
                SAXReader reader = new SAXReader();
                Document document = reader.read(configfile);
                Element root = document.getRootElement();

                port = Integer.parseInt(root.element("serverport").getText());
                iecip = root.element("iecip").getText();
                iecport = Integer.parseInt(root.element("iecport").getText());
                baudrate = Integer.parseInt(root.element("baudrate").getText());
            } catch (Exception e) {
                initDefault();
                createConfigXML();
            }
        }
    }

    /**
     * 创建配置XML
     */
    private void createConfigXML() {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("property");
        Element serverport = root.addElement("serverport");
        serverport.addText(String.valueOf(port));
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        try {
            XMLWriter writer = new XMLWriter(new FileOutputStream(filename), format);
            writer.write(document);
            writer.close();

        } catch (IOException e) {
            log(e);
        }
    }

    /**
     * 设置默认值
     */
    private void initDefault() {
        port = 3456;
        iecport = 2404;
        baudrate = 9600;
        iecip = "192.168.1.250";
    }

}
