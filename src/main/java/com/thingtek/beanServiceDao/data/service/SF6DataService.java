package com.thingtek.beanServiceDao.data.service;

import com.thingtek.beanServiceDao.base.service.BaseService;
import com.thingtek.beanServiceDao.data.dao.SF6DataDao;
import com.thingtek.beanServiceDao.data.entity.SF6DataBean;
import com.thingtek.beanServiceDao.unit.base.BaseUnitBean;
import com.thingtek.beanServiceDao.unit.entity.SF6UnitBean;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.socket.data.entity.DataSearchPara;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SF6DataService extends BaseService {

    @Resource
    private SF6DataDao dao;

    private int clttype = 1;

    @Resource
    private UnitService unitService;

    public boolean deleteData(Map<SF6UnitBean, List<Date>> datamap) {
        List<Boolean> flags = new ArrayList<>();
        for (Map.Entry<SF6UnitBean, List<Date>> entry : datamap.entrySet()) {
            try {
                flags.add(dao.deleteDatas(entry.getKey(), entry.getValue()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return !flags.contains(false);
    }

    public List<SF6DataBean> getData(DataSearchPara para) {
        List<Map<String, Object>> datas;
        try {
            datas = dao.findData(para);
        } catch (Exception e) {
            datas = new ArrayList<>();
            log(e);
        }
        List<SF6DataBean> baseDataBeanList = new ArrayList<>();
        for (Map<String, Object> one : datas) {
            if (one == null) {
                continue;
            }
            try {
                SF6DataBean dataBean = new SF6DataBean();
                dataBean.resolve(one);
                dataBean.setUnit(unitService.getUnitByNumber(para.getClttype(), dataBean.getUnit_num()));
                baseDataBeanList.add(dataBean);
            } catch (Exception e) {
                log(e);
            }
        }
        return baseDataBeanList;

    }

    public boolean saveData(SF6DataBean... data) {
        boolean flag;
        try {
            flag = dao.save(data);
        } catch (Exception e) {
            log(e);
            flag = false;
        }
        return flag;
    }

    public List<SF6DataBean> getLatestDatas() {
        List<SF6DataBean> datas = new ArrayList<>();
        try {
            List<BaseUnitBean> units = unitService.getAll(clttype);
            for (BaseUnitBean unit : units) {
                SF6DataBean data = dao.getLatest(unit.getUnit_num());
                if (data != null) {
                    data.setUnit(unit);
                    datas.add(data);
                }
            }
        } catch (Exception e) {
            log(e);
        }
        return datas;
    }

    public Float getDataByField(String field,byte unit_num){
        try {
            return dao.getDataByField(field,unit_num);
        } catch (Exception e) {
            log(e);
            return (float) 0;
        }
    }
}
