package com.thingtek.beanServiceDao.warn.entity;

import com.thingtek.beanServiceDao.unit.base.BaseUnitBean;
import lombok.Data;

import javax.swing.*;
import java.util.Date;
import java.util.Objects;
import java.util.Vector;

@Data
public class WarnBean {

    private int clt_type;
    private byte unit_num;
    private int unit_addr;
    private String warn_info;
    private Date inserttime;
    private String point_name;//0
    private String phase;//1
    private BaseUnitBean unitBean;
    private int xianquan;

    public void resolveTotalInfoTable(JTable table, int row) {
        point_name = (String) table.getValueAt(row, 0);
        phase = (String) table.getValueAt(row, 1);
//        warn_info = (String) table.getValueAt(row, 2);
        inserttime = (Date) table.getValueAt(row, 2);
    }

    public Vector<Object> getTableCol() {
        Vector<Object> vector = new Vector<>();
        vector.add(point_name);
        vector.add(phase);
//        vector.add(warn_info);
        vector.add(inserttime);
        return vector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WarnBean warnBean = (WarnBean) o;
        return clt_type == warnBean.clt_type &&
                unit_num == warnBean.unit_num;
    }

    @Override
    public int hashCode() {
        return Objects.hash(clt_type, unit_num);
    }
}
