package com.thingtek.beanServiceDao.unit.entity;

import com.thingtek.beanServiceDao.unit.base.BaseUnitBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.util.Map;
import java.util.Vector;

@EqualsAndHashCode(callSuper = true)
@Data
public class SF6UnitBean extends BaseUnitBean {

    private String com;
    private Integer diya_warn;
    private Integer bisuo_warn;
    private Integer midu_warn;

    @Override
    public void resolve(Map<String, Object> one) {
        super.resolve(one);
        com = one.get("COM") == null ? null : (String) one.get("COM");
        diya_warn = one.get("DIYA_WARN") == null ? null : (Integer) one.get("DIYA_WARN");
        bisuo_warn = one.get("BISUO_WARN") == null ? null : (Integer) one.get("BISUO_WARN");
        midu_warn = one.get("MIDU_WARN") == null ? null : (Integer) one.get("MIDU_WARN");
    }

    public void resolveTable(JTable table, int row) {
        super.resolveTable(table, row);
        Object obj;
        com = (String) table.getValueAt(row, 3);
        one.put("COM", com);
        obj = table.getValueAt(row, 4);
        diya_warn = obj == null ? null : (int) (double) (Double.parseDouble(String.valueOf(obj)) * 1000);
        one.put("DIYA_WARN", diya_warn);
        obj = table.getValueAt(row, 5);
        bisuo_warn = obj == null ? null : (int) (double) (Double.parseDouble(String.valueOf(obj)) * 1000);
        one.put("BISUO_WARN", bisuo_warn);
        obj = table.getValueAt(row, 6);
        midu_warn = obj == null ? null : (int) (double) (Double.parseDouble(String.valueOf(obj)) * 1000);
        one.put("MIDU_WARN", midu_warn);
    }

    @Override
    public Vector<Object> getTableCol() {
        Vector<Object> vector = super.getTableCol();
        vector.add(com);
        vector.add(diya_warn == null ? null : diya_warn / 1000.0);
        vector.add(bisuo_warn == null ? null : bisuo_warn / 1000.0);
        vector.add(midu_warn == null ? null : midu_warn / 1000.0);
        return vector;
    }
}
