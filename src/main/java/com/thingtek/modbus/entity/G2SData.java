package com.thingtek.modbus.entity;

import com.thingtek.beanServiceDao.data.entity.SF6DataBean;
import com.thingtek.beanServiceDao.point.entity.PointBean;
import com.thingtek.beanServiceDao.unit.entity.SF6UnitBean;
import com.thingtek.iec104.iec104.Init;
import net.sf.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class G2SData extends BaseG2S {
    @Override
    public void resolve() {
        byte unitnumaddr = bytes[0];
        int length = agreement.getOnedatalength();
        byte order = bytes[1];
        int totallength = bytes[2] & 0xff;
        Map<Byte, SF6DataBean> dataBeanMap = new HashMap<>();

        Date insettime = Calendar.getInstance().getTime();
        JSONObject modbus = Init.modbus;
//        System.out.println("dataseq:" + seq);
        for (int i = 3; i < totallength; i += length, seq++) {
            byte[] bytes = new byte[4];
            System.arraycopy(this.bytes, i, bytes, 0, bytes.length);
            float value = bytesL2Float2(bytes, 2);
            JSONObject jsonObject = modbus.getJSONObject(String.valueOf(seq));

            if (jsonObject.isEmpty()) {
                seq = 1;
                jsonObject = modbus.getJSONObject(String.valueOf(seq));
            }
//            System.out.println(jsonObject);
            byte unitnum = (byte) jsonObject.getInt("unit_num");
            String phase = jsonObject.getString("phase");
            SF6UnitBean unit = (SF6UnitBean) unitService.getUnitByNumber(clttype, unitnum);

            if (unit == null) {
                unit = new SF6UnitBean();
                int point_num = jsonObject.getInt("point_num");
                PointBean pointBean = pointService.getPointByNum(clttype, point_num);
                if (pointBean != null) {
                    unit.setPoint(pointBean);
                }
                unit.setUnit_num(unitnum);
                unit.setPhase(phase);
            }

            SF6DataBean dataBean = dataBeanMap.get(unitnum);
            if (dataBean == null) {
                dataBean = new SF6DataBean();
                dataBeanMap.put(unitnum, dataBean);
                dataBean.setUnit_num(unitnum);
                dataBean.setUnit(unit);
                dataBean.setInserttime(insettime);
            }
            String field = jsonObject.getString("field");
            switch (field) {
                case "YALI":
                    dataBean.setYali((float) ((int)(value*1)/100.0));
                    break;
                case "MIDU":
                    dataBean.setMidu((float) ((int)(value/10)/100.0));
                    break;
                case "WENDU":
                    dataBean.setWendu(value);
                    break;
            }
        }
        if (seq >= agreement.getCount()) {
            seq = 1;
        }
        SF6DataBean[] dataBeans = dataBeanMap.values().toArray(new SF6DataBean[0]);
        dataService.saveData(dataBeans);
        for (SF6DataBean dataBean : dataBeans) {
//            System.out.println(dataBean);
            addData(dataBean);
        }
       /* short yali = bytes2short(agreement.getYalioff(), length, bytes);
        short midu = bytes2short(agreement.getMiduoff(), length, bytes);
        short wendu = bytes2short(agreement.getWenduoff(), length, bytes);
        SF6DataBean dataBean = new SF6DataBean();
        dataBean.setUnit_num(unitnum);
        dataBean.setYali(yali);
        dataBean.setMidu(midu);
        dataBean.setWendu(wendu);
        Date date = Calendar.getInstance().getTime();
        dataBean.setInserttime(date);
        dataService.saveData(dataBean);
        BaseUnitBean unitBean = unitService.getUnitByNumber(clttype, unitnum);
        if (unitBean != null) {
            SF6UnitBean unit = (SF6UnitBean) unitBean;
            dataBean.setUnit(unitBean);
            addData(dataBean);
            if (yali < unit.getBisuo_warn()
                    || yali < unit.getDiya_warn()
                    || midu < unit.getMidu_warn()) {
                WarnBean warnBean = new WarnBean();
                warnBean.setClt_type(clttype);
                warnBean.setUnit_num(unitnum);
                warnBean.setUnitBean(unit);
                warnBean.setInserttime(date);
                StringBuilder stringBuilder = new StringBuilder();
                if (yali < unit.getBisuo_warn()) {
                    stringBuilder.append("--闭锁报警");
                } else if (yali < unit.getDiya_warn()) {
                    stringBuilder.append("--低压报警");
                }
                if (midu < unit.getMidu_warn()) {
                    stringBuilder.append("--低密度报警");
                }
                warnBean.setWarn_info(stringBuilder.toString());
                warnService.save(warnBean);
                addWarn(warnBean);
                shell.showWarn(clttype);
            }
        }*/

    }
}
