package com.thingtek.view.shell.dataCollect;

import com.thingtek.beanServiceDao.data.base.BaseDataBean;
import com.thingtek.beanServiceDao.data.entity.SF6DataBean;
import com.thingtek.beanServiceDao.data.service.SF6DataService;
import com.thingtek.beanServiceDao.point.entity.PointBean;
import com.thingtek.beanServiceDao.warn.entity.WarnBean;
import com.thingtek.view.component.layout.ModifiedFlowLayout;
import com.thingtek.view.shell.dataCollect.base.BaseCollectPanel;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class SF6DataCollectPanel extends BaseCollectPanel {

    public SF6DataCollectPanel() {
        clttype = 1;
        ABCUnits = new ArrayList<>();
    }

    @Resource
    private SF6DataService dataService;

    private JPanel center;

    private java.util.List<SF6ABCUnitPanel> ABCUnits;

    @Override
    public SF6DataCollectPanel init() {
        super.init();
        setLayout(new BorderLayout());
        center = new JPanel(new ModifiedFlowLayout(10, 10, 10));
        center.setOpaque(false);
        JScrollPane jsp = new JScrollPane(center);
        jsp.setOpaque(false);
        jsp.getViewport().setOpaque(false);
        this.add(jsp);
        refreshPoint();
        return this;
    }

    @Override
    public void refreshPoint() {
        center.removeAll();
        ABCUnits.clear();
        java.util.List<PointBean> points = pointService.getPoints(clttype);
        for (PointBean point : points) {
            SF6ABCUnitPanel abc = new SF6ABCUnitPanel();
            abc.setPoint(point);
            abc.init();
            center.add(abc);
            ABCUnits.add(abc);
        }
        validate();
    }


    @Override
    public void addData(BaseDataBean data) {
        for (SF6ABCUnitPanel abcUnit : ABCUnits) {
            if (data.getUnit().getPoint() != null && Objects.equals(abcUnit.getPoint().getPoint_num(),
                    data.getUnit().getPoint().getPoint_num())) {
                abcUnit.addData(data);
            }
        }
    }

    @Override
    public void addWarn(WarnBean warnBean) {

    }

    @Override
    public void refreshData() {
        java.util.List<SF6DataBean> datas = dataService.getLatestDatas();
        for (SF6DataBean data : datas) {
            addData(data);
        }
    }

    @Override
    public void refreshWarn() {

    }

}
