package com.thingtek.iec104.entity;

import com.thingtek.beanServiceDao.unit.base.BaseUnitBean;
import com.thingtek.beanServiceDao.warn.entity.WarnBean;
import com.thingtek.iec104.iec104.Init;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class G2SYX extends BaseG2S {
    @Override
    public void resolve() {
        List<Byte> list = new ArrayList<>();
        list.add((byte) 0);
        list.add((byte) 0);
        list.add((byte) 0);
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) count);
        list.add((byte) 20);
        list.add((byte) 0);
        list.add((byte) publicaddr);
        list.add((byte) (publicaddr >> 8));
        list.add((byte) addr);
        list.add((byte) (addr >> 8));
        list.add((byte) (addr >> 16));
        list.add((byte) 0);
        addr += 3;
        list.add((byte) addr);
        list.add((byte) (addr >> 8));
        list.add((byte) (addr >> 16));
        list.add((byte) 0);
//        System.out.println(addr);
        /*for (int i = 0; i < count; i++, addr++) {
            JSONObject jsonObject = Init.remoteSignal.getJSONObject(String.valueOf(addr));
            if (jsonObject == null || jsonObject.isEmpty()) {
                continue;
            }
            int point_num = jsonObject.getInt("point_num");
//            System.out.println(point_num);
            String phase = jsonObject.getString("phase");
//            System.out.println(phase);
            BaseUnitBean unit = unitService.getUnitByPointAndPhase(4, point_num, phase);
            if (unit == null) {
//                System.out.println("unit = null");
                list.add((byte) addr);
                list.add((byte) (addr >> 8));
                list.add((byte) (addr >> 16));
                list.add((byte) 0);
                continue;
            }
            WarnBean warnBean = warnService.getWarnByUnit(4, unit.getUnit_num());
            if (warnBean == null) {
//                System.out.println("warn = null");
                list.add((byte) addr);
                list.add((byte) (addr >> 8));
                list.add((byte) (addr >> 16));
                list.add((byte) 0);
                continue;
            }
            Date date = warnBean.getInserttime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            if (calendar.before(Calendar.getInstance())) {
                continue;
            }
            list.add((byte) addr);
            list.add((byte) (addr >> 8));
            list.add((byte) (addr >> 16));
            list.add((byte) 1);
        }*/
        result = new byte[list.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = list.get(i);
        }

    }

    @Override
    public byte[] getResult() {
        return result;
    }
}
