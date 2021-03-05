package com.thingtek.modbus.serialPort;

import com.thingtek.beanServiceDao.base.service.BaseService;
import com.thingtek.beanServiceDao.data.service.SF6DataService;
import com.thingtek.beanServiceDao.point.service.PointService;
import com.thingtek.beanServiceDao.unit.base.BaseUnitBean;
import com.thingtek.beanServiceDao.unit.entity.SF6UnitBean;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.beanServiceDao.warn.service.WarnService;
import com.thingtek.modbus.agreement.ModbusAgreement;
import com.thingtek.config.PortConfig;
import com.thingtek.view.logo.LogoInfo;
import com.thingtek.view.shell.Shell;
import com.thingtek.view.shell.debugs.Debugs;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.comm.SerialPort;
import java.util.ArrayList;
import java.util.List;

@Component
public class SerialConnect extends BaseService {

    private int clttype = 1;

    public int getClttype() {
        return clttype;
    }

    @Resource
    private SerialTool serialTool;
    @Resource
    private Debugs debugs;

    @Resource
    private UnitService unitService;
    @Resource
    private SF6DataService dataService;
    @Resource
    private WarnService warnService;
    @Resource
    private PointService pointService;
    @Resource
    private PortConfig portConfig;
    @Resource
    private ModbusAgreement agreement;
    @Resource
    private LogoInfo logoInfo;
    @Resource
    private Shell shell;
    private List<SerialThread> serialThreads;

    private List<Thread> threads;

    public SerialConnect() {
        serialThreads = new ArrayList<>();
        threads = new ArrayList<>();
    }

    public void openSerialPorts() {
        List<String> coms = getSaveUnitComs();
        for (String com : coms) {
            try {
                SerialPort port = serialTool.openPort(com, portConfig.getBaudrate());
                if(port==null){
                    continue;
                }
                SerialThread serialThread = new SerialThread();
                serialThread.setSerialPort(port);
                serialThread.setAgreement(agreement);
                serialThread.setDebugs(debugs);
                serialThread.setUnitService(unitService);
                serialThread.setDataService(dataService);
                serialThread.setPointService(pointService);
                serialThread.setWarnService(warnService);
                serialThread.setLogoInfo(logoInfo);
                serialThreads.add(serialThread);
                Thread thread = new Thread(serialThread);
                thread.start();
                threads.add(thread);
            } catch (Exception e) {
                log(e);
            }
        }
    }


    private List<String> getSaveUnitComs() {
        List<String> coms = new ArrayList<>();
        List<BaseUnitBean> units = unitService.getAll(clttype);
        if (units == null || units.size() == 0) {
            return coms;
        }
        for (BaseUnitBean unit : units) {
            SF6UnitBean sf6unit = (SF6UnitBean) unit;
            String com = sf6unit.getCom();
            if (com == null || "".equals(com) || coms.contains(com)) {
                continue;
            }
            coms.add(com);
        }
        return coms;
    }


    public void closeConnect() {
        for (SerialThread s : serialThreads) {
            try {
                s.getIn().close();
                s.getOut().close();
                serialTool.closePort(s.getSerialPort());
            } catch (Exception e) {
//                log(e);
            }
        }
        serialThreads.clear();
        for (Thread thread : threads) {
            try {
                thread.interrupt();
            } catch (Exception e) {
//                log(e);
            }
        }
        threads.clear();
    }


}
