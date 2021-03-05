package com.thingtek.beanServiceDao.unit.dao;

import com.thingtek.beanServiceDao.unit.base.BaseUnitBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UnitDao {


    List<Map<String, Object>> findByCltType(int clttype) throws Exception;

    boolean saveUnit(@Param("clt_type") int clttype, @Param("units") BaseUnitBean... unitBean) throws Exception;

    boolean update(@Param("clt_type")int clttype, @Param("unit")Map<String, Object> one) throws Exception;

    boolean deleteUnitByNum(@Param("clt_type") int clttype, @Param("unit_nums") byte... unit_num) throws Exception;

}
