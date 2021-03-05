package com.thingtek.view.component.tablemodel;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Vector;
@Component
public class SF6DataTableModel extends BaseTableModel {
    public SF6DataTableModel() {
        super();
        typeArray = new Class[]{
                Byte.class,
                String.class,
                String.class,
                Float.class,
                Float.class,
                Float.class,
                Date.class
        };
    }
    @Override
    protected void initDefault() {
        super.initDefault();
        Vector<String> column = new Vector<String>();
        column.add("单元地址");
        column.add("位置名称");
        column.add("相位");
        column.add("压力(MPa)");
        column.add("密度(MPa)");
        column.add("温度(℃)");
        column.add("时间");
        this.setDataVector(row, column);
    }
}
