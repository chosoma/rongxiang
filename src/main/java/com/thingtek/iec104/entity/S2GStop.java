package com.thingtek.iec104.entity;

public class S2GStop extends BaseS2G {
    @Override
    public byte[] getResult() {
        return bytes;
    }

    @Override
    public void resolve() {
        super.resolve();
        bytes[0] = 0x23;
    }
}
