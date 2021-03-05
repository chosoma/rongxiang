package com.thingtek.iec104.entity;
/*
启动
 */
public class S2GWoShou extends BaseS2G {

    public S2GWoShou() {
        control = 0x0B;
    }

    @Override
    public void resolve() {
        super.resolve();
        bytes[0] = 0x0B;
    }

    @Override
    public byte[] getResult() {
//        return new byte[]{0x0b, 0x00, 0x00, 0x00};
        return bytes;
    }
}
