package com.thingtek.iec104.entity;

public class S2GCS extends BaseS2G {
    public S2GCS() {
        control = (byte) 0x83;
    }

    @Override
    public byte[] getResult() {
        return bytes;
    }

    @Override
    public void resolve() {
        super.resolve();
        bytes[0] = (byte) 0x83;
    }
}
