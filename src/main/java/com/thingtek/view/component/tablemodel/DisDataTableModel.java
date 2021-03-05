package com.thingtek.view.component.tablemodel;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Vector;
@Component
public class DisDataTableModel extends BaseTableModel {
    public DisDataTableModel() {
        super();
        typeArray = new Class[]{
                Byte.class,
                String.class,
                String.class,
                Date.class
        };
    }

    @Override
    protected void initDefault() {
        super.initDefault();
        Vector<String> column = new Vector<String>();
        column.add("单元编号");
        column.add("位置名称");
        column.add("相位");
        column.add("时间");
        this.setDataVector(row, column);
    }
}
