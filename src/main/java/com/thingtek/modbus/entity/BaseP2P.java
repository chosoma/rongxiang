package com.thingtek.modbus.entity;

import com.thingtek.beanServiceDao.data.entity.SF6DataBean;
import com.thingtek.beanServiceDao.data.service.SF6DataService;
import com.thingtek.beanServiceDao.point.service.PointService;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.beanServiceDao.warn.entity.WarnBean;
import com.thingtek.beanServiceDao.warn.service.WarnService;
import com.thingtek.modbus.agreement.ModbusAgreement;
import com.thingtek.view.logo.LogoInfo;
import com.thingtek.view.shell.Shell;
import com.thingtek.view.shell.dataCollect.DataCollectPanel;
import com.thingtek.view.shell.dataCollect.base.BaseCollectPanel;

import java.math.BigDecimal;


public abstract class BaseP2P {

    protected Shell shell;

    protected int clttype = 1;
    protected LogoInfo logoInfo;

    public void setLogoInfo(LogoInfo logoInfo) {
        this.logoInfo = logoInfo;
    }

    protected DataCollectPanel collectPanel;

    public void setCollectPanel(DataCollectPanel collectPanel) {
        this.collectPanel = collectPanel;
    }

    protected UnitService unitService;

    public void setShell(Shell shell) {
        this.shell = shell;
    }

    protected SF6DataService dataService;

    public void setUnitService(UnitService unitService) {
        this.unitService = unitService;
    }

    public void setDataService(SF6DataService dataService) {
        this.dataService = dataService;
    }
protected PointService pointService;

    public PointService getPointService() {
        return pointService;
    }

    public void setPointService(PointService pointService) {
        this.pointService = pointService;
    }

    protected WarnService warnService;

    public void setWarnService(WarnService warnService) {
        this.warnService = warnService;
    }

    protected ModbusAgreement agreement;

    public BaseP2P setAgreement(ModbusAgreement agreement) {
        this.agreement = agreement;
        return this;
    }

    public abstract void resolve();

    protected void addData(SF6DataBean data) {
        for (BaseCollectPanel collectPanel : logoInfo.getCollectPanelMap().values()) {
            if (clttype == collectPanel.getClttype()) {
                collectPanel.addData(data);
            }
        }
    }

    protected void addWarn(WarnBean warn) {
        for (BaseCollectPanel collectPanel : logoInfo.getCollectPanelMap().values()) {
            if (clttype == collectPanel.getClttype()) {
                collectPanel.addWarn(warn);
            }
        }
    }

    protected byte[] int2bytes(int i, int length) {
        byte[] bytes = new byte[length];
        for (int j = 0; j < length; j++) {
            bytes[j] = (byte) (i >> (length - j - 1) * 8);
        }
        return bytes;
    }

    protected int bytes2int(int off, byte[] bytes) {
        int i = 0;
        for (int j = 0; j < 4; j++) {
            i |= (bytes[off + j] & 0xff) << (j * 8);
        }
        return i;
    }

    protected short bytes2short(int off, int length, byte[] bytes) {
        short i = 0;
        for (int j = 0; j < length; j++) {
            i |= (bytes[off + j] & 0xff) << (j * 8);
        }
        return i;
    }

    protected Float bytesL2Float2(byte[] b, int scale) {
        int temp = 0;
        for (int i = 0; i < 4; i++) {
            temp = temp | (b[i] & 0xFF) << (8 * i);
        }
//        System.out.println(Integer.toHexString(temp));
        Float f = Float.intBitsToFloat(temp);
        if (Float.isNaN(f)) {
            return 0f;
        }
        BigDecimal bd = new BigDecimal(f);
        return bd.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
