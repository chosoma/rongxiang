package com.thingtek.socket.data.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

public @Data
class DataSearchPara {

    private Byte unit_num;

    private List<Byte> unitnums;

    private int startcount;

    private Date T1;

    private Date T2;

    private int clttype;

}
