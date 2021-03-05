package com.thingtek.modbus.entity;

public abstract class BaseG2S extends BaseP2P {
    protected byte[] bytes;
    protected int seq = 1;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public BaseG2S setBytes(byte[] bytes) {
        this.bytes = bytes;
        return this;
    }
}
