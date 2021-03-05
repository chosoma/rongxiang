package com.thingtek.iec104.entity;

public abstract class BaseG2S extends BaseP2P {

    protected byte[] bytes;

    protected int addr;

    protected int publicaddr;

    public void setPublicaddr(int publicaddr) {
        this.publicaddr = publicaddr;
    }

    public void setCount(int count) {
        this.count = count;
    }

    protected int count;

    public void setAddr(int addr) {
        this.addr = addr;
    }

    public BaseG2S setBytes(byte[] bytes) {
        this.bytes = bytes;
        return this;
    }

    protected byte[] result;

    public abstract byte[] getResult();
}
