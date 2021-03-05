package com.thingtek.beanServiceDao.unit.base;

import com.thingtek.beanServiceDao.clt.entity.CltBean;
import com.thingtek.beanServiceDao.point.entity.PointBean;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public abstract class BaseUnitBean {
    protected int id;
    protected Byte unit_num;//单元编号
    protected PointBean point;//测点
    protected Integer point_num;
    protected String phase;
    protected Map<String, Object> one;
    protected CltBean clt;

    protected BaseUnitBean() {
        one = new HashMap<>();
    }

    public void resolve(Map<String, Object> one) {
        this.one = one;
        id = (int) (Integer) one.get("ID");
        unit_num = (byte) (int) (Integer) one.get("UNIT_NUM");
        point_num = (Integer) one.get("POINT_NUM");
        phase = one.get("PHASE") == null ? "" : (String) one.get("PHASE");
    }

    public void resolve2map() {
        one.put("UNIT_NUM", unit_num);
        one.put("POINT_NUM", point_num);
        one.put("PHASE", phase);
    }

    public void resolveTable(JTable table, int row) {
        phase = (String) table.getValueAt(row, 2);
        one.put("PHASE", phase);
    }

    public Vector<Object> getTableCol() {
        Vector<Object> vector = new Vector<>();
        vector.add(unit_num);
        vector.add(point.getPoint_name());
        vector.add(phase);
        return vector;
    }

    public Object get(String key) {
        return one.get(key);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Byte getUnit_num() {
        return unit_num;
    }

    public void setUnit_num(Byte unit_num) {
        this.unit_num = unit_num;
    }

    public PointBean getPoint() {
        return point;
    }

    public void setPoint(PointBean point) {
        this.point = point;
    }

    public Integer getPoint_num() {
        return point_num;
    }

    public void setPoint_num(Integer point_num) {
        this.point_num = point_num;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public Map<String, Object> getOne() {
        return one;
    }

    public void setOne(Map<String, Object> one) {
        this.one = one;
    }

    public CltBean getClt() {
        return clt;
    }

    public void setClt(CltBean clt) {
        this.clt = clt;
    }
}
