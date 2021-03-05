package com.thingtek.beanServiceDao.warn.service;

import com.thingtek.beanServiceDao.base.service.BaseService;
import com.thingtek.beanServiceDao.point.entity.PointBean;
import com.thingtek.beanServiceDao.point.service.PointService;
import com.thingtek.beanServiceDao.unit.base.BaseUnitBean;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.beanServiceDao.warn.dao.WarnDao;
import com.thingtek.beanServiceDao.warn.entity.WarnBean;
import com.thingtek.socket.data.entity.DataSearchPara;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class WarnService extends BaseService {

    private @Resource
    WarnDao dao;

    private @Resource
    UnitService unitService;

    private @Resource
    PointService pointService;

    private boolean setWarn(WarnBean warn) {
        try {
            BaseUnitBean unit = unitService.getUnitByNumber(warn.getClt_type(), warn.getUnit_num());
            PointBean point = pointService.getPointByNum(warn.getClt_type(), unit.getPoint_num());
            warn.setPoint_name(point.getPoint_name());
            warn.setPhase(unit.getPhase());
            return true;
        } catch (Exception e) {
            log(e);
            return false;
        }
    }

    public List<WarnBean> getWarnByPara(DataSearchPara para) {
        List<WarnBean> warns = new ArrayList<>();
        try {
            List<WarnBean> getwarns = dao.getByPara(para);
            for (WarnBean warn : getwarns) {
                if (setWarn(warn)) {
                    warns.add(warn);
                }
            }
        } catch (Exception e) {
            log(e);
        }
        return warns;
    }

    public WarnBean getWarnByUnit(int clttype, byte unit_num) {
        try {
            return dao.getLastByUnit(clttype,unit_num);
        } catch (Exception e) {
            log(e);
            return null;
        }
    }

    public List<WarnBean> getLastWarn() {
        List<WarnBean> warns = new ArrayList<>();
        try {
            List<WarnBean> getwarns = dao.getLast();
            for (WarnBean warn : getwarns) {
                if (setWarn(warn)) {
                    warns.add(warn);
                }
            }
        } catch (Exception e) {
            log(e);
        }
        return warns;
    }

    public List<WarnBean> getLastWarn(int clttype) {
        List<WarnBean> warns = new ArrayList<>();
        for (BaseUnitBean unitBean : unitService.getAll(clttype)) {
            try {
                WarnBean warnBean = dao.getLastByUnit(clttype, unitBean.getUnit_num());
                if (warnBean != null) {
                    warnBean.setUnitBean(unitBean);
                    warns.add(warnBean);
                }
            } catch (Exception e) {
                log(e);
            }
        }
        return warns;
    }

    public boolean deleteWarn(Map<WarnBean, List<Date>> warnDateMapList) {
        List<Boolean> flags = new ArrayList<>();
        for (Map.Entry<WarnBean, List<Date>> entry : warnDateMapList.entrySet()) {
            try {
                flags.add(dao.delete(entry.getKey(), entry.getValue()));
            } catch (Exception e) {
                log(e);
                flags.add(false);
            }
        }
        return !flags.contains(false);
    }

    public boolean save(WarnBean... warns) {
        try {
            return dao.save(warns);
        } catch (Exception e) {
            log(e);
            return false;
        }
    }
}
