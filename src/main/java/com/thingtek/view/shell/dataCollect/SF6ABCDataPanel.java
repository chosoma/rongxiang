package com.thingtek.view.shell.dataCollect;


import com.thingtek.beanServiceDao.data.entity.SF6DataBean;
import com.thingtek.beanServiceDao.warn.entity.WarnBean;

import javax.swing.*;
import java.awt.*;

public class SF6ABCDataPanel extends JPanel {
    private String[] phases;
    private JLabel[][] jldatas;
    private Color colorWarn = new Color(255, 80, 0),
            colorB = new Color(255, 255, 255),
            buttonColor = new Color(240, 240, 240);

    public SF6ABCDataPanel() {

        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(new BorderLayout());
        phases = new String[]{"A", "B", "C"};
        colors = new Color[]{
                new Color(158, 158, 0),
                new Color(0, 158, 0),
                new Color(158, 0, 0)};
        setVisible(false);
        initTitle();
        initLeft();
        initCenter();
//        setBackground(colorWarn);

    }

    private JLabel title;

    private void initTitle() {
        title = new JLabel(" ", JLabel.CENTER);
        this.add(title, BorderLayout.NORTH);
    }

    private void initCenter() {
        JPanel center = new JPanel(new GridLayout(phases.length, 1));
        String[] titles = new String[]{"压力:", "密度:", "温度:"};
        jldatas = new JLabel[phases.length][titles.length];
        JLabel[][] jltitles = new JLabel[phases.length][titles.length];
        for (int i = 0; i < phases.length; i++) {
            JPanel jPanel = new JPanel(new GridLayout(phases.length, 2));
            for (int j = 0; j < titles.length; j++) {
                jltitles[i][j] = new JLabel(titles[j], JLabel.CENTER);
                jltitles[i][j].setForeground(colors[i]);
                jPanel.add(jltitles[i][j]);
                jldatas[i][j] = new JLabel("  ", JLabel.CENTER);
                jldatas[i][j].setForeground(colors[i]);
                jPanel.add(jldatas[i][j]);
            }
            center.add(jPanel);
        }

        this.add(center, BorderLayout.CENTER);
    }

    private Color[] colors;

    private void initLeft() {
        JPanel left = new JPanel(new GridLayout(phases.length, 1));

        Font font = new Font(null, Font.PLAIN, 25);
        JLabel[] jlphase = new JLabel[phases.length];
        for (int i = 0; i < jlphase.length; i++) {
            jlphase[i] = new JLabel(phases[i], JLabel.CENTER);
            jlphase[i].setFont(font);
            jlphase[i].setForeground(colors[i]);
            left.add(jlphase[i]);
        }
        this.add(left, BorderLayout.WEST);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void addData(SF6DataBean dataBean) {
        int index = getIndex(dataBean.getUnit().getPhase());
        if (dataBean.getYali() != null) {
            jldatas[index][0].setText(String.valueOf(dataBean.getYali()));
        }
        if (dataBean.getMidu() != null) {
            jldatas[index][1].setText(String.valueOf(dataBean.getMidu()));
        }
        if (dataBean.getWendu() != null) {
            jldatas[index][2].setText(String.valueOf(dataBean.getWendu()));
        }
    }

    public void startWarn(WarnBean warnBean) {
        int index = getIndex(warnBean.getPhase());
        for (JLabel jLabel : jldatas[index]) {
            jLabel.setBackground(colorWarn);
            jLabel.setOpaque(true);
        }
    }

    private int getIndex(String phase) {
        int index = 0;
        for (int i = 0; i < phases.length; i++) {
            if (phases[i].equals(phase)) {
                return i;
            }
        }
        return index;
    }


}
