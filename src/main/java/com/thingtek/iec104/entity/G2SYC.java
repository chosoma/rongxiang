package com.thingtek.iec104.entity;

import com.thingtek.beanServiceDao.unit.base.BaseUnitBean;
import com.thingtek.iec104.iec104.Init;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class G2SYC extends BaseG2S {
    @Override
    public void resolve() {
        List<Byte> list = new ArrayList<>();
        list.add((byte) 0);
        list.add((byte) 0);
        list.add((byte) 0);
        list.add((byte) 0);
        list.add((byte) 13);
        list.add((byte) (0x80 + count));
        list.add((byte) 20);
        list.add((byte) 0);
        list.add((byte) publicaddr);
        list.add((byte) (publicaddr >> 8));
        list.add((byte) addr);
        list.add((byte) (addr >> 8));
        list.add((byte) (addr >> 16));
        for (int i = 0; i < count; i++, addr++) {
            JSONObject jsonObject = Init.remoteMeasure.getJSONObject(String.valueOf(addr));
            if (jsonObject == null || jsonObject.isEmpty()) {
                continue;
            }
            String field = jsonObject.getString("field");
//            System.out.println(field);
            int point_num = jsonObject.getInt("point_num");
//            System.out.println(point_num);
            String phase = jsonObject.getString("phase");
//            System.out.println(phase);
            BaseUnitBean unit = unitService.getUnitByPointAndPhase(1, point_num, phase);
            if (unit == null) {
                continue;
            }
            Float floatvalue = dataService.getDataByField(field, unit.getUnit_num());
            if (floatvalue == null) {
                floatvalue = (float) 0;
            }
            int value = Float.floatToIntBits(floatvalue);
//            System.out.println(floatvalue);

            list.add((byte) value);
            list.add((byte) (value >> 8));
            list.add((byte) (value >> 16));
            list.add((byte) (value >> 24));
            list.add((byte) 0);

        }
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
