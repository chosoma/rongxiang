package com.thingtek.view.component.tablemodel;

import lombok.Data;

import java.util.Map;

public
@Data
class TableConfig {

    private Map<String, BaseTableModel> dataTableModels;

    public BaseTableModel getDataModel(String type) {
        return dataTableModels.get(type);
    }

    private Map<String, BaseTableModel> unitTableModels;

    public BaseTableModel getUnitModel(String type) {
        return unitTableModels.get(type);
    }

    private Map<String, String> decimalreg;

    public String getDecimalReg(String string) {
        return decimalreg.get(string);
    }

    private String datereg;


}
