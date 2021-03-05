package com.thingtek.view.shell.dataCollect;

import com.thingtek.beanServiceDao.data.base.BaseDataBean;
import com.thingtek.beanServiceDao.data.entity.SF6DataBean;
import com.thingtek.beanServiceDao.point.entity.PointBean;
import com.thingtek.beanServiceDao.unit.base.BaseUnitBean;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;

public class SF6ABCUnitPanel extends JPanel {

    private JLabel[][] jlas = new JLabel[3][3]; //0 密度 1 压力 2 温度
    //    private JLabel[] jlbs = new JLabel[3]; //0 密度 1 压力 2 温度
//    private JLabel[] jlcs = new JLabel[3]; //0 密度 1 压力 2 温度
    private String[] phasestrings = new String[]{"A", "B", "C"};

    private List<BaseUnitBean> units;
    private PointBean point;

    public void setUnits(List<BaseUnitBean> units) {
        this.units = units;
    }

    public void setPoint(PointBean point) {
        this.point = point;
    }

    public List<BaseUnitBean> getUnits() {
        return units;
    }

    public PointBean getPoint() {
        return point;
    }

    public SF6ABCUnitPanel() {
        setLayout(null);
        setOpaque(false);
    }

    public void init() {
        initTitle();
        initCenter();
    }

    private void initTitle() {
        JLabel title = new JLabel(point.getPoint_name(), JLabel.CENTER);
        title.setOpaque(true);
        title.setBackground(colorTitle);
        title.setBounds(0, 0, 200, 21);
        title.setBorder(border);
        this.add(title, BorderLayout.NORTH);
        int x = 0;
        int y = 20;
        int width = 51;
        int height = 21;
        JLabel[] sublables = new JLabel[4];
        String[] substrings = new String[]{"相位", "压力", "密度", "温度"};
        for (int i = 0; i < sublables.length; i++) {
            sublables[i] = new JLabel(substrings[i], JLabel.CENTER);
            sublables[i].setOpaque(true);
            sublables[i].setBackground(colorSubTitle);
            if (i == sublables.length - 1) {
                width = 50;
            }
            sublables[i].setBounds(x, y, width, height);
            sublables[i].setBorder(border);
            x += width - 1;
            this.add(sublables[i]);
        }
        x = 0;
        y += height - 1;
        width = 51;
        JLabel[] phaselabels = new JLabel[3];
        for (int i = 0; i < phaselabels.length; i++) {
            phaselabels[i] = new JLabel(phasestrings[i], JLabel.CENTER);
            phaselabels[i].setOpaque(true);
            phaselabels[i].setBackground(colorSubTitle);
            phaselabels[i].setBounds(x, y, width, height);
            phaselabels[i].setBorder(border);
            y += height - 1;
            this.add(phaselabels[i]);
        }

    }

    private void initCenter() {
        int y = 40, height = 21;
        for (int i = 0; i < jlas.length; i++) {
            int width = 51, x = 50;
            for (int j = 0; j < jlas[i].length; j++) {
                jlas[i][j] = new JLabel("", JLabel.CENTER);
                jlas[i][j].setOpaque(true);
                jlas[i][j].setBackground(Color.WHITE);
                if (j == jlas[i].length - 1) {
                    width = 50;
                }
                jlas[i][j].setBounds(x, y, width, height);
                jlas[i][j].setBorder(border);
                x += width - 1;
                this.add(jlas[i][j]);
            }
            y += height - 1;
        }
    }

    public void addData(BaseDataBean dataBean) {
        int index = getPhaseIndex(dataBean.getUnit().getPhase());
        if (index == -1) {
            return;
        }
        SF6DataBean data = (SF6DataBean) dataBean;
        if (data.getYali() != null) {
            jlas[index][0].setText(String.valueOf(data.getYali()));
        }
        if (data.getMidu() != null) {
            jlas[index][1].setText(String.valueOf(data.getMidu()));
        }
        if (data.getWendu() != null) {
            jlas[index][2].setText(String.valueOf(data.getWendu()));
        }
    }

    public int getPhaseIndex(String phase) {
        for (int i = 0; i < phasestrings.length; i++) {
            if (phasestrings[i].equals(phase)) {
                return i;
            }
        }
        return -1;
    }

    private static Color colorTitle = new Color(138, 191, 237);// 138, 191, 237
    private static Color colorSubTitle = new Color(182, 216, 245);// 182, 216, 245
    private static Border border = BorderFactory.createLineBorder(
            new Color(127, 157, 185), 1);

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 110);
    }
}
