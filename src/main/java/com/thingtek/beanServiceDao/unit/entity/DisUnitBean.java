package com.thingtek.beanServiceDao.unit.entity;

import com.thingtek.beanServiceDao.unit.base.BaseUnitBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.util.Map;
import java.util.Vector;

@EqualsAndHashCode(callSuper = true)
@Data
public class DisUnitBean extends BaseUnitBean {

    private Integer fz;
    private String ip;

    public void resolve2map() {
        super.resolve2map();
        one.put("FZ", fz);
        one.put("IP", ip);
    }

    public void resolveTable(JTable table, int row) {
        super.resolveTable(table, row);
        String strfz = String.valueOf(table.getValueAt(row, 3));
        fz = strfz.equals("") ? null : Integer.parseInt(strfz);
        one.put("FZ", fz);
        ip = (String) table.getValueAt(row, 4);
        one.put("IP", ip);
    }

    public void resolve(Map<String, Object> one) {
        super.resolve(one);
        fz = (Integer) (one.get("FZ") == null ? 3000 : one.get("FZ"));
        ip = (String) one.get("IP");
    }

    public Vector<Object> getTableCol() {
        Vector<Object> vector = super.getTableCol();
        vector.add(fz);
        vector.add(ip);
        return vector;
    }

}
