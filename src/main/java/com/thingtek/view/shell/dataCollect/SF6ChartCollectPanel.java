package com.thingtek.view.shell.dataCollect;

import com.thingtek.beanServiceDao.data.base.BaseDataBean;
import com.thingtek.beanServiceDao.data.entity.SF6DataBean;
import com.thingtek.beanServiceDao.data.service.SF6DataService;
import com.thingtek.beanServiceDao.point.entity.PointBean;
import com.thingtek.beanServiceDao.unit.base.BaseUnitBean;
import com.thingtek.beanServiceDao.unit.entity.SF6UnitBean;
import com.thingtek.beanServiceDao.warn.entity.WarnBean;
import com.thingtek.view.component.listener.MouseMoveListenAdapter;
import com.thingtek.view.shell.dataCollect.base.BaseCollectPanel;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;

public class SF6ChartCollectPanel extends BaseCollectPanel {

    private JPanel center;
    private Image image;
    @Resource
    private SF6DataService dataService;

    public SF6ChartCollectPanel() {
        clttype = 1;
        buttonList = new ArrayList<>();
    }

    private java.util.List<ChartIconLabel> buttonList;

    @Override
    public SF6ChartCollectPanel init() {
        super.init();
        setLayout(new BorderLayout());
        image = factorys.getIconFactory().getImage("background");
        center = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                if (image != null) {
                    g.drawImage(image, 0, 0, center.getWidth(), center.getHeight(), center);
                }
                setShowBounds();
            }
        };
        JScrollPane jScrollPane = new JScrollPane(center);
//        center.setOpaque(false);
        add(jScrollPane, BorderLayout.CENTER);
        refreshPoint();
        center.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setButtonBounds();
            }
        });
        return this;
    }

    @Override
    public void refreshPoint() {
        center.removeAll();
        buttonList.clear();
        java.util.List<PointBean> points = pointService.getPoints(clttype);
        ImageIcon icon = factorys.getIconFactory().getIcon("sf6");
        for (final PointBean point : points) {
            final ChartIconLabel button = new ChartIconLabel(point.getPoint_name(), icon);
            button.setPointBean(point);
            buttonList.add(button);
            final SF6ABCDataPanel datapanel = new SF6ABCDataPanel();
            datapanel.setTitle(point.getPoint_name());
            button.setDataPanel(datapanel);
            MouseMoveListenAdapter mmla = new MouseMoveListenAdapter(button, center);
            if (admin) {
                button.addListeners(mmla);
            }
            mmla.setPointBean(point);
            mmla.setPointService(pointService);
            center.add(button);
            center.add(datapanel);
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
//                    System.out.println("enter");
                    datapanel.setVisible(true);
                    button.stopWarning();
                }

                @Override
                public void mouseExited(MouseEvent e) {
//                    System.out.println("exited");
                    datapanel.setVisible(false);
                }
            });
        }
        setButtonBounds();
        center.validate();
        center.updateUI();
    }

    @Override
    public void addData(BaseDataBean data) {
        for (ChartIconLabel button : buttonList) {
            PointBean point = button.getPointBean();
            if (Objects.equals(point.getPoint_num(), data.getUnit().getPoint_num())) {
                SF6ABCDataPanel datapanel = (SF6ABCDataPanel) button.getDataPanel();
                datapanel.addData((SF6DataBean) data);
                return;
            }
        }
    }

    @Override
    public void addWarn(WarnBean warnBean) {
        BaseUnitBean unit = warnBean.getUnitBean();
        if (unit == null) {
            return;
        }
        for (ChartIconLabel button : buttonList) {
            PointBean pointBean = button.getPointBean();
            if (Objects.equals(pointBean.getPoint_num(), unit.getPoint_num())) {
                button.startWarning();
                SF6ABCDataPanel dataPanel = (SF6ABCDataPanel) button.getDataPanel();
                dataPanel.startWarn(warnBean);
            }
        }
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
        java.util.List<WarnBean> warns = warnService.getLastWarn(clttype);
        for (WarnBean warnBean : warns) {
            addWarn(warnBean);
        }
    }


    private void setButtonBounds() {
        for (ChartIconLabel button : buttonList) {
            PointBean point = button.getPointBean();
            button.setBounds((int) (point.getX() * center.getWidth()), (int) (point.getY() * center.getHeight()), 50, 50);
        }
    }

    private void setShowBounds() {
        int width = center.getWidth();
        int height = center.getHeight();
        int panelwidth = 125;
        int panelheight = 225;
        for (ChartIconLabel button : buttonList) {
            int panelx = button.getX() + button.getWidth();
            int panely = button.getY() + button.getHeight();
            if (panelx + panelwidth > width && panely + panelheight > height) {
                panelx = button.getX() - panelwidth;
                panely = height - panelheight;
            } else if (panelx + panelwidth > width) {
                panelx = width - panelwidth;
            } else if (panely + panelheight > height) {
                panely = height - panelheight;
            }
            button.getDataPanel().setBounds(panelx, panely, panelwidth, panelheight);
        }
    }

}
