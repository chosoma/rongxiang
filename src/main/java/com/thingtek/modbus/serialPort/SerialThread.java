package com.thingtek.modbus.serialPort;

import com.thingtek.beanServiceDao.base.service.BaseService;
import com.thingtek.beanServiceDao.data.service.SF6DataService;
import com.thingtek.beanServiceDao.point.service.PointService;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.beanServiceDao.warn.service.WarnService;
import com.thingtek.modbus.agreement.ModbusAgreement;
import com.thingtek.modbus.entity.BaseG2S;
import com.thingtek.view.logo.LogoInfo;
import com.thingtek.view.shell.debugs.Debugs;

import javax.comm.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//import java.net.Socket;
import java.util.*;

public class SerialThread extends BaseService implements Runnable {
    private int clttype = 1;

    private SerialPort serialPort;
    private ModbusAgreement agreement;
    private Debugs debugs;
    private SF6DataService dataService;
    private WarnService warnService;
    private UnitService unitService;
    private LogoInfo logoInfo;
private PointService pointService;

    public PointService getPointService() {
        return pointService;
    }

    public void setPointService(PointService pointService) {
        this.pointService = pointService;
    }

    public void setLogoInfo(LogoInfo logoInfo) {
        this.logoInfo = logoInfo;
    }

    public void setWarnService(WarnService warnService) {
        this.warnService = warnService;
    }

    public void setDataService(SF6DataService dataService) {
        this.dataService = dataService;
    }

    public void setUnitService(UnitService unitService) {
        this.unitService = unitService;
    }

    public void setAgreement(ModbusAgreement agreement) {
        this.agreement = agreement;
    }

    public void setDebugs(Debugs debugs) {
        this.debugs = debugs;
    }

    public void setSerialPort(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    public SerialPort getSerialPort() {
        return serialPort;
    }

    private byte[] cache;
    private InputStream in;
    private OutputStream out;

    public InputStream getIn() {
        return in;
    }

    public OutputStream getOut() {
        return out;
    }


    @Override
    public void run() {
        try {
            in = serialPort.getInputStream();
            out = serialPort.getOutputStream();
//            Socket socket = new Socket("192.168.1.250", 8080);
//            in = socket.getInputStream();
//            out = socket.getOutputStream();
        } catch (IOException e) {
//            log(e);
            run();
        }
        write();

        read();

    }

    private void write() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
//                System.out.println(seq);
                byte[] b = agreement.getWrite(seq);
                debugs.send(b, b.length, serialPort.getName());
                try {
                    out.write(b);
                } catch (IOException e) {
//                    log(e);
                }
            }
        }, 5000,agreement.getDelay());
    }

    private int seq = 1;

    private void read() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] bytes = new byte[1024 * 1024];
                int commit = -1;
                try {
                    int available = in.available();
//                        System.out.println("available:" + available);
//                        System.out.println("commit:" + commit);
                    while ((commit = in.read(bytes)) != -1) {
                        byte[] b = new byte[commit];
                        System.arraycopy(bytes, 0, b, 0, b.length);
                        debugs.rec(b, b.length, Calendar.getInstance().getTime(), serialPort.getName());
                        if (cache == null || cache.length == 0) {
                            cache = b;
                        } else {
                            int length = cache.length;
                            cache = Arrays.copyOf(cache, length + b.length);
                            System.arraycopy(b, 0, cache, length, b.length);
                        }
                        if (!agreement.checkCRC16_X(cache)) continue;
                        BaseG2S g2s = agreement.getG2S(agreement.getAddr(), cache);
                        g2s.setSeq(seq);
                        g2s.setUnitService(unitService);
                        g2s.setDataService(dataService);
                        g2s.setWarnService(warnService);
                        g2s.setPointService(pointService);
                        g2s.resolve();
                        seq = g2s.getSeq();
                        cache = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    ;

    /*private void resolveCache() {
        int off = agreement.getoff(cache);
        if (off < 0) {
            return;
        }

        int datalength = cache[off + 2] & 0xff;
        int length = datalength + 4;
        byte[] bytes = new byte[length];
        System.arraycopy(cache, off, bytes, 0, bytes.length);
        byte addr = bytes[agreement.getAddroff()];
        byte order = bytes[agreement.getOrderoff()];
        byte[] datas = new byte[datalength];
        System.arraycopy(bytes, off + 3, datas, 0, datas.length);

        SF6UnitBean unit = (SF6UnitBean) unitService.getUnitByNumber(clttype, addr);
        SF6DataBean data = new SF6DataBean();
        if (unit != null) {
            data.setUnit(unit);
            data.setUnit_num(unit.getUnit_num());
        }
        short yali = bytes2short(agreement.getYalioff(), datas);
        data.setYali(yali);
        short midu = bytes2short(agreement.getMiduoff(), datas);
        data.setMidu(midu);
        short wendu = bytes2short(agreement.getWenduoff(), datas);
        data.setWendu(wendu);
        dataService.saveData(data);
        addData(data);
        *//*if (yali > agreement.getYalimax()
                || yali < agreement.getYalimin()
                || midu > agreement.getMidumax()
                || midu < agreement.getMidumin()
                || wendu > agreement.getWendumax()
                || wendu < agreement.getWendumin()) {
            WarnBean warnBean = new WarnBean();
            warnBean.setUnitBean(unit);
            warnBean.setUnit_addr(addr);
            StringBuilder stringBuilder = new StringBuilder();
            if (yali > agreement.getYalimax()) {
                stringBuilder.append("--压力过高");
            } else if (yali < agreement.getYalimin()) {
                stringBuilder.append("--压力过低");
            }
            if (midu > agreement.getMidumax()) {
                stringBuilder.append("--密度过高");
            } else if (midu < agreement.getMidumin()) {
                stringBuilder.append("--密度过低");
            }
            if (wendu > agreement.getMidumax()) {
                stringBuilder.append("--温度过高");
            } else if (wendu < agreement.getMidumin()) {
                stringBuilder.append("--温度过低");
            }
            warnBean.setWarn_info(stringBuilder.toString());
            warnService.save(warnBean);
            addWarn(warnBean);
            shell.showWarn(clttype);
        }*//*

        byte[] other = new byte[cache.length - bytes.length];
        System.arraycopy(cache, off + bytes.length, other, 0, other.length);
        cache = other;
        resolveCache();
    }

    private void addData(SF6DataBean data) {
        for (BaseCollectPanel collectPanel : logoInfo.getCollectPanelMap().values()) {
            if (clttype == collectPanel.getClttype()) {
                collectPanel.addData(data);
            }
        }
    }

    private void addWarn(WarnBean warn) {
        for (BaseCollectPanel collectPanel : logoInfo.getCollectPanelMap().values()) {
            if (clttype == collectPanel.getClttype()) {
                collectPanel.addWarn(warn);
            }
        }
    }

    private short bytes2short(int off, byte[] bytes) {
        short i = 0;
        for (int j = 0; j < 2; j++) {
            i |= (bytes[off + j] & 0xff) << (j * 8);
        }
        return i;
    }*/
}
