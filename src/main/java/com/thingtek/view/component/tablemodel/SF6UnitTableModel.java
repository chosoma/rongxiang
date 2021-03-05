package com.thingtek.view.component.tablemodel;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.Vector;

@Component
public class SF6UnitTableModel extends BaseTableModel {
    public SF6UnitTableModel() {
        super();
        typeArray = new Class[]{
                Byte.class,
                JComboBox.class,
                JComboBox.class,
//                Integer.class,
                JComboBox.class,
                Float.class,
                Float.class,
                Float.class,
        };
    }

    @Override
    protected void initDefault() {
        super.initDefault();
        Vector<String> column = new Vector<String>();
        column.add("单元地址");
//        column.add("单元名称");
        column.add("位置名称");
        column.add("相位");
//        column.add("IEC地址");
        column.add("串口");
        column.add("低压报警值(MPa)");
        column.add("闭锁报警值(MPa)");
        column.add("密度报警值(MPa)");
        this.setDataVector(row, column);
    }


    @Override
    public boolean isCellEditable(int row, int col) {
        return col != 0;
    }
}
