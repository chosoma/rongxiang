package com.thingtek.iec104.entity;

import com.thingtek.iec104.iec104.IecSocket;
import com.thingtek.iec104.iec104.IecSocketThread;

public abstract class BaseS2G extends BaseP2P {

    protected byte control;

    protected byte datalength;

    protected byte[] bytes;

    protected IecSocketThread iecSocket;

    public void setIecSocket(IecSocketThread iecSocket) {
        this.iecSocket = iecSocket;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public void resolve() {
        datalength = bytes[agreement.getDatalengthoff()];
    }

    protected byte[] result;

    public abstract byte[] getResult();

}
