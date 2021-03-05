package com.thingtek.socket.entity;

public class G2SHeart extends BaseG2S {
    @Override
    public void resolve() {
        unitnum = bytes[unitnumoff];
        BaseS2G s2g = agreementConfig.getS2G("heart");
        s2g.setUnitnum(unitnum);
        result = s2g.getResult();
        cansend = true;
    }

    private byte[] result;

    @Override
    public byte[] getResult() {
        return result;
    }
}
