package com.thingtek.iec104.iec104;

import com.thingtek.iec104.iec104.util.FileUtils;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Init {

    public static Properties typeIdProp = null;
    public static Properties causeProp = null;
    public static JSONObject devsinfo = null;
    public static JSONObject remoteSignal = null;
    public static JSONObject remoteMeasure = null;
    public static JSONObject remoteControl = null;
    public static JSONObject remoteAdjust = null;
    public static JSONObject modbus = null;

    public static void start() {
        initBusinessData();
    }

    /* 配置文件解析 */
    public static void initBusinessData() {
        try {
            // 传送类型及原因
            typeIdProp = FileUtils.loadPropFile("typeId.properties");
            causeProp = FileUtils.loadPropFile("cause.properties");
            // 设备信息
//            devsinfo = FileUtils.loadJsonFile("dev.json");
            // 遥信、遥测、遥控、遥调
            remoteMeasure = FileUtils.loadJsonFile("remote_measure.json");
            remoteSignal = FileUtils.loadJsonFile("remote_signal.json");
            modbus = FileUtils.loadJsonFile("modbus.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /* 数据库初始化 */
}

