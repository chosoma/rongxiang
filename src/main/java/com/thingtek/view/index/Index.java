package com.thingtek.view.index;


import com.thingtek.beanServiceDao.data.entity.DisDataBean;
import com.thingtek.beanServiceDao.data.entity.SF6DataBean;
import com.thingtek.beanServiceDao.data.service.DisDataService;
import com.thingtek.beanServiceDao.data.service.SF6DataService;
import com.thingtek.beanServiceDao.unit.entity.DisUnitBean;
import com.thingtek.beanServiceDao.unit.entity.SF6UnitBean;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.iec104.iec104.IecSocket;
import com.thingtek.modbus.serialPort.SerialConnect;
import com.thingtek.socket.CollectServer;
import com.thingtek.socket.data.entity.DataSearchPara;
import lombok.Data;
import com.thingtek.view.login.Loading;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
public @Data
class Index {

    @Resource
    private Loading loading;

    @Resource
    private CollectServer server;

    @Resource
    private SerialConnect serialConnect;

    @Resource
    private IecSocket iec104port;

    @Resource
    private SF6DataService sf6DataService;
    @Resource
    private DisDataService disDataService;
    @Resource
    private UnitService unitService;

    public void init() {
//        JfreeChartUtil.setChartTheme();
        loading.init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                server.openConnection();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                serialConnect.openSerialPorts();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                iec104port.init();
            }
        }).start();
//        clear();
    }

    private void clear() {
        clearsf6();
        cleardis();


    }

    private void cleardis() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_MONTH, -90);
                DataSearchPara dsp = new DataSearchPara();
                dsp.setT2(c.getTime());
                dsp.setClttype(4);
                List<DisDataBean> datas = disDataService.getDataInfo(dsp);
                Map<DisUnitBean, List<Date>> datamap = new HashMap<>();
                for (DisDataBean data : datas) {
                    DisUnitBean unit = (DisUnitBean) unitService.getUnitByNumber(4, data.getUnit_num());
                    if (unit != null && datamap.containsKey(unit)) {
                        datamap.get(unit).add(data.getInserttime());
                    } else {
                        List<Date> dates = new ArrayList<>();
                        dates.add(data.getInserttime());
                        datamap.put(unit, dates);
                    }
                }
                disDataService.deleteData(datamap);
            }
        }, 0, 24 * 60 * 60 * 1000);
    }

    private void clearsf6() {
        new Timer().schedule(new TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_MONTH, -90);
                DataSearchPara dsp = new DataSearchPara();
                dsp.setT2(c.getTime());
                dsp.setClttype(1);
                List<SF6DataBean> datas = sf6DataService.getData(dsp);
                Map<SF6UnitBean, List<Date>> datamap = new HashMap<>();
                for (SF6DataBean data : datas) {
                    SF6UnitBean unit = (SF6UnitBean) unitService.getUnitByNumber(1, data.getUnit_num());
                    if (unit != null && datamap.containsKey(unit)) {
                        datamap.get(unit).add(data.getInserttime());
                    } else {
                        List<Date> dates = new ArrayList<>();
                        dates.add(data.getInserttime());
                        datamap.put(unit, dates);
                    }
                }
                sf6DataService.deleteData(datamap);

            }
        }, 0, 24 * 60 * 60 * 1000);
    }

}
