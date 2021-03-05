package com.thingtek.modbus.entity;

import com.thingtek.beanServiceDao.unit.base.BaseUnitBean;
import com.thingtek.beanServiceDao.unit.entity.SF6UnitBean;

public class G2SYaliWarn extends BaseG2S {
    @Override
    public void resolve() {
        byte unitnum = bytes[0];
        int length = agreement.getOnedatalength();
        short yali = bytes2short(agreement.getYaliwarnoff(), length, bytes);
        BaseUnitBean unit = unitService.getUnitByNumber(clttype, unitnum);
        SF6UnitBean sf6UnitBean = (SF6UnitBean) unit;
        sf6UnitBean.setDiya_warn(yali & 0xFFFF);
        sf6UnitBean.setBisuo_warn(yali - 100);
        unitService.updateUnit(clttype, sf6UnitBean);
    }

}
