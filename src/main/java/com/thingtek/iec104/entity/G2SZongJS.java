package com.thingtek.iec104.entity;
/*
总召唤结束
 */
public class G2SZongJS extends BaseG2S {
    @Override
    public byte[] getResult() {
        return result;
    }

    @Override
    public void resolve() {
        result = new byte[this.bytes.length];
        System.arraycopy(bytes, 0, result, 0, result.length);
        result[6] = 10;
    }
}
