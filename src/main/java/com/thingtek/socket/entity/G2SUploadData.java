package com.thingtek.socket.entity;

import com.thingtek.beanServiceDao.data.entity.DisDataBean;
import com.thingtek.socket.RawData;

import java.util.Calendar;

public class G2SUploadData extends BaseG2S {

    private byte bigseq;
    private int bigseqoff = 1;
    private int smallseq;
    private int smallseqoff = 4;
    private int smallseqlength = 2;
    private int totalsmallseq;
    private int totalsmallseqoff = 2;
    private int totalsmallseqlenght = 2;
    private int datalengthoff = 6;
    private int datalengthlength = 2;
    private byte[] datas;
    private int dataoff = 8;
    private DisDataBean data;

    @Override
    public void resolve() {
        super.resolve();
//        System.out.println(Arrays.toString(datas));
        data = new DisDataBean();
        unitnum = bytes[unitnumoff];
        data.setUnit_num(unitnum);
        bigseq = bytes[bigseqoff];
        totalsmallseq = bytes2int(totalsmallseqoff, totalsmallseqlenght, bytes);
        smallseq = bytes2int(smallseqoff, smallseqlength, bytes);
        int datalength = bytes2int(datalengthoff, datalengthlength, bytes);
        if (smallseq == totalsmallseq) {
            datas = new byte[datalength - 5];
            byte electric = bytes[bytes.length - 1];
            data.setElectric(electric);
        } else {
            datas = new byte[datalength];
        }
//        System.out.println("总包数:" + totalsmallseq);
//        System.out.println("当前包序号:" + smallseq);
        System.arraycopy(bytes, dataoff, datas, 0, datas.length);
        data.resolve(this.datas);
        data.setInserttime(Calendar.getInstance().getTime());
        RawData rawdata = new RawData(bigseq, smallseq, totalsmallseq, data);
        BaseS2G s2g = agreementConfig.getS2G("uploaddata");
//        System.out.println("G2SUploadData:"+rawdata);
        if (dataBuffer.receDatas(rawdata)) {
            s2g.setCmdtype((byte) 0x10);
        } else {
            s2g.setCmdtype((byte) 0x11);
        }
        s2g.setUnitnum(unitnum);
        s2g.setDatas(new byte[]{bigseq});
        s2g.resolve();
        result = s2g.getResult();
        cansend = totalsmallseq == smallseq;
    }

    private byte[] result;

    @Override
    public byte[] getResult() {
        return result;
    }
}
