package com.thingtek.iec104.entity;

/**
 * 总召唤
 */
public class S2GZong extends BaseS2G {


    private byte count;

    private int publicaddr;
    private int addr;

    @Override
    public void resolve() {
        super.resolve();
        result = new byte[this.bytes.length];
        System.arraycopy(bytes, 0, result, 0, result.length);
        this.result[6] = 7;

        count = bytes[5];
//        System.out.println("count:" + count);
        publicaddr = bytes[8] & 0xff | (bytes[9] & 0xff << 8);
//        System.out.println("publicaddr:" + publicaddr);
        addr = bytes[10] & 0xff | ((bytes[11] & 0xff) << 8) | ((bytes[12] & 0xff) << 16);
//        System.out.println("addr:" + addr);

        iecSocket.clear();
        if (addr >= agreement.getYcmin() && addr <= agreement.getYcmax()) {
            yc();
        }if (addr >= agreement.getYxmin() && addr <= agreement.getYxmax()) {
            //yx();
        }
        if (addr == 0) {
            addr = agreement.getYcmin();
            byte max = 48;
            for (int yccount = (byte) (agreement.getYcmax() - agreement.getYcmin()+1);
                 yccount > 0; yccount -= max, addr += max) {
                if (yccount > max) {
                    count = max;
                } else {
                    count = (byte) yccount;
                }
                yc();
            }
            addr = agreement.getYxmin();
            count = (byte) (agreement.getYxmax() - agreement.getYxmin()+1);
        //    yx();
        }
        G2SZongJS zongJS = new G2SZongJS();
        zongJS.setBytes(bytes);
        zongJS.resolve();
        byte[] bytes = zongJS.getResult();
        iecSocket.addwrite(bytes);
    }

    @Override
    public byte[] getResult() {
        return result;
    }

    private void yc() {
        G2SYC yc = new G2SYC();
        yc.setAgreement(agreement);
        yc.setDataService(dataService);
        yc.setUnitService(unitService);
        yc.setWarnService(warnService);
        yc.setAddr(addr);
        yc.setPublicaddr(publicaddr);
        yc.setCount(count);
        yc.resolve();
        byte[] bytes = yc.getResult();
        iecSocket.addwrite(bytes);
    }

    private void yx() {
        G2SYX yx = new G2SYX();
        yx.setAgreement(agreement);
        yx.setDataService(dataService);
        yx.setUnitService(unitService);
        yx.setWarnService(warnService);
        yx.setAddr(addr);
        yx.setPublicaddr(publicaddr);
        yx.setCount(count);
        yx.resolve();
        byte[] bytes = yx.getResult();
        iecSocket.addwrite(bytes);
    }

}
