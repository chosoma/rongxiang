package com.thingtek.iec104.agreement;

import com.thingtek.beanServiceDao.base.service.BaseService;
import com.thingtek.beanServiceDao.data.service.SF6DataService;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.beanServiceDao.warn.service.WarnService;
import com.thingtek.iec104.entity.BaseS2G;

import javax.annotation.Resource;
import java.util.Map;

public class IECAgreementConfig extends BaseService {

    private int yxmax;
    private int ycmax;
    private int yxmin;
    private int ycmin;

    public int getYxmax() {
        return yxmax;
    }

    public void setYxmax(int yxmax) {
        this.yxmax = yxmax;
    }

    public int getYcmax() {
        return ycmax;
    }

    public void setYcmax(int ycmax) {
        this.ycmax = ycmax;
    }

    public int getYxmin() {
        return yxmin;
    }

    public void setYxmin(int yxmin) {
        this.yxmin = yxmin;
    }

    public int getYcmin() {
        return ycmin;
    }

    public void setYcmin(int ycmin) {
        this.ycmin = ycmin;
    }

    private int headoff;

    private byte head;

    private int datalengthoff;

    private int iorderoff;

    public int getIorderoff() {
        return iorderoff;
    }

    public void setIorderoff(int iorderoff) {
        this.iorderoff = iorderoff;
    }

    public int getSuorderoff() {
        return suorderoff;
    }

    public void setSuorderoff(int suorderoff) {
        this.suorderoff = suorderoff;
    }

    private int suorderoff;

    private int SUzhen;

    private Map<Integer, Integer> ordermap;

    private Map<Integer, String> s2gmap;

    public int getSUzhen() {
        return SUzhen;
    }

    public void setSUzhen(int SUzhen) {
        this.SUzhen = SUzhen;
    }


    public int getheadindex(byte[] bytes) {
        int off = -1;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == head) {
                off = i;
                return off;
            }
        }
        return off;
    }


    public Map<Integer, String> getS2gmap() {
        return s2gmap;
    }

    public void setS2gmap(Map<Integer, String> s2gmap) {
        this.s2gmap = s2gmap;
    }

    private Map<Integer, Integer> yuanyin;

    public void setYuanyin(Map<Integer, Integer> yuanyin) {
        this.yuanyin = yuanyin;
    }

    public Map<Integer, Integer> getYuanyin() {
        return yuanyin;
    }

    public Map<Integer, Integer> getOrdermap() {
        return ordermap;
    }

    public void setOrdermap(Map<Integer, Integer> ordermap) {
        this.ordermap = ordermap;
    }

    public int getHeadoff() {
        return headoff;
    }

    public void setHeadoff(int headoff) {
        this.headoff = headoff;
    }

    public byte getHead() {
        return head;
    }

    public void setHead(byte head) {
        this.head = head;
    }

    public int getDatalengthoff() {
        return datalengthoff;
    }

    public void setDatalengthoff(int datalengthoff) {
        this.datalengthoff = datalengthoff;
    }

    @Resource
    private SF6DataService sf6DataService;
    @Resource
    private WarnService warnService;
    @Resource
    private UnitService unitService;

    public BaseS2G gets2g(int order) {
        BaseS2G s2g = null;
//        System.out.println(Integer.toHexString(order));
        try {
            s2g = (BaseS2G) this.getClass()
                    .getClassLoader()
                    .loadClass(s2gmap.get(order))
                    .newInstance();
            s2g.setAgreement(this);
            s2g.setDataService(sf6DataService);
            s2g.setWarnService(warnService);
            s2g.setUnitService(unitService);
        } catch (Exception e) {
            log(e);
        }
        return s2g;
    }

}
