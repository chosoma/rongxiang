package com.thingtek.iec104.iec104.util;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/* @Description */
public class FileUtils {
	/* 加载property配置文件 */
    public static Properties loadPropFile(String filePath) throws Exception{
        InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(filePath);
        InputStreamReader isr = new InputStreamReader(is, "GBK");
        Properties properties = new Properties();
        try {
            properties.load(isr);
        }catch (IOException ex){
            ex.printStackTrace();
        }finally {
            is.close();
            isr.close();
        }
        return properties;
    }
    /* 加载json配置文件 */
    public static JSONObject loadJsonFile(String filePath) throws Exception{
        InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuffer jsonStrBuff = new StringBuffer();
        String brStr = null;
        while ((brStr =br.readLine()) != null){
            jsonStrBuff.append(brStr);
        }
        return JSONObject.fromObject(jsonStrBuff.toString());
    }
}
