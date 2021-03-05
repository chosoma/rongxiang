package com.thingtek.beanServiceDao.data.dao;

import com.thingtek.beanServiceDao.data.entity.SF6DataBean;
import com.thingtek.beanServiceDao.unit.entity.SF6UnitBean;
import com.thingtek.socket.data.entity.DataSearchPara;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SF6DataDao {

    List<Map<String, Object>> findData(DataSearchPara para) throws Exception;

    boolean deleteDatas(@Param("unit") SF6UnitBean unit, @Param("dates") List<Date> dates) throws Exception;

    boolean save(SF6DataBean... dataBean) throws Exception;

    SF6DataBean getLatest(byte unit_num) throws Exception;

    Float getDataByField(@Param("field") String field, @Param("unit_num") byte unit_num) throws Exception;
}
