package com.thingtek.modbus.entity;

import com.thingtek.beanServiceDao.unit.base.BaseUnitBean;
import com.thingtek.beanServiceDao.unit.entity.SF6UnitBean;

public class G2SMiduWarn extends BaseG2S {
    @Override
    public void resolve() {
        byte unitnum = bytes[0];
        int length = agreement.getOnedatalength();
        short midu = bytes2short(agreement.getMiduwarnoff(), length, bytes);
        BaseUnitBean unit = unitService.getUnitByNumber(clttype, unitnum);
        SF6UnitBean sf6UnitBean = (SF6UnitBean) unit;
        sf6UnitBean.setMidu_warn(midu & 0xFFFF);
        unitService.updateUnit(clttype, sf6UnitBean);
    }

}
