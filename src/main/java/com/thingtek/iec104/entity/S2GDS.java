package com.thingtek.iec104.entity;

public class S2GDS extends BaseS2G {

    @Override
    public byte[] getResult() {
        return bytes;
    }

    @Override
    public void resolve() {
        bytes[6] = 7;
    }
}
