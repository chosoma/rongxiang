package com.thingtek.beanServiceDao.data.entity;

import com.thingtek.beanServiceDao.data.base.BaseDataBean;
import com.thingtek.socket.agreement.SocketAgreement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.util.Date;
import java.util.Vector;

/**
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DisDataBean extends BaseDataBean {

    private SocketAgreement agreementConfig;
    private int[] data;
    private int datacount;
    private int value;
    private String datastring;
    private byte electric;

    public void setDatastring(String datastring) {
        this.datastring = datastring;
        String[] strs = datastring.split(",");
//            System.out.println(strs.length);
        data = new int[strs.length];
        for (int i = 0; i < strs.length; i++) {
            data[i] = Integer.parseInt(strs[i]);
        }
    }

    public void resolve(byte[] datas) {
        this.data = resolveData2Line(datas);
    }

    public void resolveTotalInfoTable(JTable table, int selectrow) {
        super.resolveTotalInfoTable(table, selectrow);
        this.inserttime = (Date) table.getValueAt(selectrow, 3);
    }

    /*protected float bytes2float(byte[] b, int off, int length, int scale) {
        int temp = 0;
        for (int i = off, index = 0; i < off + length; i++, index++) {
            temp |= (b[i] & 0xFF) << (8 * index);
        }
        Float f = Float.intBitsToFloat(temp);
        if (Float.isNaN(f)) {
            return 0f;
        }
        BigDecimal bd = new BigDecimal(f);
        return bd.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }*/

    public int[] resolveData2Line(byte[] datas) {
        int[] dataint = new int[datas.length / 2];
        for (int i = 0; i < dataint.length; i++) {
            dataint[i] = bytes2int(i * 2, 2, datas);
        }
        return dataint;
    }

    @Override
    public Vector<Object> getDataTotalCol() {
        Vector<Object> vector = super.getDataTotalCol();
        vector.add(inserttime);
        return vector;
    }
}
