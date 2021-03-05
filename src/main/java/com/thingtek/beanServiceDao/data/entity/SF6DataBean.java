package com.thingtek.beanServiceDao.data.entity;

import com.thingtek.beanServiceDao.data.base.BaseDataBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

@EqualsAndHashCode(callSuper = true)
@Data
public class SF6DataBean extends BaseDataBean {

    private Float yali;
    private Float midu;
    private Float wendu;

    @Override
    public void resolve(Map<String, Object> one) {
        super.resolve(one);
        yali = one.get("YALI") == null ? 0 : (Float) one.get("YALI");
        midu = one.get("MIDU") == null ? 0 : (Float) one.get("MIDU");
        wendu = one.get("WENDU") == null ? 0 : (Float) one.get("WENDU");
    }

    @Override
    public void resolveTotalInfoTable(JTable table, int selectrow) {
        super.resolveTotalInfoTable(table, selectrow);
        this.inserttime = (Date) table.getValueAt(selectrow, 6);
    }

    @Override
    public Vector<Object> getDataTotalCol() {
        Vector<Object> vector = super.getDataTotalCol();
        vector.add(yali);
        vector.add(midu);
        vector.add(wendu);
        vector.add(inserttime);
        return vector;
    }
}
