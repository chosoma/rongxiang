package com.thingtek.beanServiceDao.data.service;

import com.thingtek.beanServiceDao.base.service.BaseService;
import com.thingtek.beanServiceDao.data.dao.DisDataDao;
import com.thingtek.beanServiceDao.data.entity.DisDataBean;
import com.thingtek.beanServiceDao.unit.entity.DisUnitBean;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.socket.data.entity.DataSearchPara;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class DisDataService extends BaseService {

    @Resource
    private DisDataDao dao;

    @Resource
    private UnitService unitService;

    public List<DisDataBean> getDataInfo(DataSearchPara para) {
        List<Map<String, Object>> datas;
        try {
            datas = dao.findDataInfo(para);
        } catch (Exception e) {
            datas = new ArrayList<>();
            log(e);
        }
        List<DisDataBean> baseDataBeanList = new ArrayList<>();
        for (Map<String, Object> one : datas) {
            try {
                if (one == null) {
                    continue;
                }
                DisDataBean dataBean = new DisDataBean();
                dataBean.resolve(one);
                dataBean.setUnit(
                        (DisUnitBean)
                                unitService.getUnitByNumber(
                                        para.getClttype(),
                                        dataBean.getUnit_num()
                                ));
                baseDataBeanList.add(dataBean);
            } catch (Exception e) {
                log(e);
            }
        }
        return baseDataBeanList;
    }

    public DisDataBean getData(DisDataBean datapara) {
        DisDataBean datareturn = new DisDataBean();
        try {
//            int[] datas = new int[60000];
//            long start = System.currentTimeMillis();
            String str = dao.findData(datapara.getUnit_num(), datapara.getInserttime());
//            System.out.println(System.currentTimeMillis() - start);
            datareturn.setInserttime(datapara.getInserttime());
            datareturn.setUnit(unitService.getUnitByNumber(4, datapara.getUnit_num()));
            datareturn.setDatastring(str);

//            datareturn.setData(datas);
        } catch (Exception e) {
            log(e);
            e.printStackTrace();
        }
        return datareturn;
    }

    public boolean deleteData(Map<DisUnitBean, List<Date>> datas) {
//        List<Boolean> flags = new ArrayList<>();
        Set<Map.Entry<DisUnitBean, List<Date>>> entries = datas.entrySet();
        List<Long> ids = new ArrayList<>();
        for (Map.Entry<DisUnitBean, List<Date>> entry : entries) {
            /*Map<String, Object> map = new HashMap<>();
            map.put("unit", entry.getKey());
            map.put("dates", entry.getValue());*/
//            System.out.println(entry.getKey());
            try {
                ids.addAll(dao.findIds(entry.getKey(), entry.getValue()));
//                flags.add(dao.deleteDatas(entry.getKey(), entry.getValue()));
            } catch (Exception e) {
//                flags.add(false);
                log(e);
            }
        }
//        System.out.println(ids);
        if (ids.size() == 0) {
            return false;
        }
        return deleteByID(ids);
//        return !flags.contains(false);
    }

    public boolean deleteByID(List<Long> ids) {
        try {
            return dao.deleteDatasById(ids);
        } catch (Exception e) {
            log(e);
            return false;
        }
    }

    public boolean saveData(DisDataBean... datas) {
        List<Boolean> flags = new ArrayList<>();
        for (DisDataBean data : datas) {
            try {
                flags.add(dao.saveData(data)
//                        && dao.saveGoo(data)
                );
            } catch (Exception e) {
                log(e);
                flags.add(false);
            }
        }
        return !flags.contains(false);
    }

}
