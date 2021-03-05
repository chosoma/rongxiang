package com.thingtek.modbus.agreement;

import com.thingtek.beanServiceDao.base.service.BaseService;
import com.thingtek.modbus.entity.BaseG2S;
import com.thingtek.view.logo.LogoInfo;
import com.thingtek.view.shell.Shell;
import com.thingtek.view.shell.dataCollect.DataCollectPanel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.annotation.Resource;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class ModbusAgreement extends BaseService {

    private @Resource
    DataCollectPanel collectPanel;
    private @Resource
    Shell shell;
    private @Resource
    LogoInfo logoInfo;
    private int addroff;
    private int orderoff;
    private int delay;
    private int wenduoff;
    private int yalioff;
    private int miduoff;
    private int onedatalength;
    private int yaliwarnoff;
    private int miduwarnoff;
    private byte order;
    private byte unitnum;
    //    private int total;
    private short addr;
    private int count;


    private Map<Short, Short> addr_num_map;
    private Map<Short, Boolean> addr_count_map;

    private Map<Short, String> s2gmap;

    public int getoff(byte[] bytes) {
        int off = -1;
        if (bytes.length < 3) {
            return off;
        }
        for (int i = 0; i < bytes.length - 2; i++) {
            int length = bytes[i + 2] & 0xff;
            if (bytes.length < length + 4) {
                continue;
            }
            byte[] bs = new byte[length + 4];
            System.arraycopy(bytes, i, bs, 0, bs.length);
            if (checkCRC16_X(bs)) {
                off = i;
                break;
            }
        }
        return off;
    }

    public synchronized BaseG2S getG2S(short addr, byte[] bytes) {

        try {
            BaseG2S g2s = (BaseG2S) this.getClass()
                    .getClassLoader()
                    .loadClass(s2gmap.get(addr))
                    .newInstance();
            g2s.setAgreement(this);
            g2s.setCollectPanel(collectPanel);
            g2s.setShell(shell);
            g2s.setLogoInfo(logoInfo);
            return g2s.setBytes(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] getWrite(int seq) {
        byte[] write = new byte[8];
        write[0] = this.unitnum;
        write[1] = order;
        int addrseq = addr + (seq - 1) * onedatalength / 2;
//        System.out.println("addrseq:" + Integer.toHexString(addrseq));
        write[3] = (byte) (addrseq & 0xff);
        write[2] = (byte) (addrseq >> 8 & 0xff);
        int countseq = (count - (seq - 1)) * onedatalength / 2;
//        System.out.println("countseq:" + Integer.toHexString(countseq));
        write[5] = (byte) (countseq & 0xff);
        write[4] = (byte) (countseq >> 8 & 0xff);
        cal_serv_crc(write);
        return write;
    }

    private void cal_serv_crc(byte[] bytes) {
        int crc16 = cal_serv_crc(bytes, bytes.length - 2);
        bytes[bytes.length - 2] = (byte) (crc16 & 0xFF);
        bytes[bytes.length - 1] = (byte) (crc16 >> 8 & 0xFF);
    }

    public boolean checkCRC16_X(byte[] bytes) {
        if (bytes.length < 2) {
            return false;
        }
        int crc16 = cal_serv_crc(bytes, bytes.length - 2);
//        System.out.println("校验：" + Integer.toHexString(crc16));
        return (bytes[bytes.length - 2] == (byte) (crc16 & 0xFF))
                && (bytes[bytes.length - 1] == (byte) (crc16 >> 8 & 0xFF));
    }

    private int cal_serv_crc(byte[] bytes, int len) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;

        int i, j;
        for (i = 0; i < len; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        return CRC;
    }

    public static void main(String[] args) {
        byte[] bytes = new byte[]{0x01, 0x04, 0x0c, 0x33, 0x33, 0x73, 0x40, 0x00, 0x00, 0x14, 0x42, (byte) 0x9a, (byte) 0x99, (byte) 0xc5, 0x41};
        System.out.println(Integer.toHexString(new ModbusAgreement().cal_serv_crc(bytes, bytes.length)));
    }

}
