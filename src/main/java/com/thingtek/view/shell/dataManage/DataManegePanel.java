package com.thingtek.view.shell.dataManage;

import com.thingtek.beanServiceDao.data.entity.SF6DataBean;
import com.thingtek.beanServiceDao.data.service.SF6DataService;
import com.thingtek.beanServiceDao.unit.entity.SF6UnitBean;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.socket.data.entity.DataSearchPara;
import com.thingtek.view.component.panel.Check2SPinner;
import com.thingtek.view.component.tablecellrander.TCR;
import com.thingtek.view.component.tablemodel.SF6DataTableModel;
import com.thingtek.view.shell.BasePanel;
import com.thingtek.view.shell.DataPanel;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class DataManegePanel extends BasePanel implements DataPanel {
    @Resource
    private SF6DataTableModel tableModel;
    @Resource
    private UnitService unitService;
    @Resource
    private SF6DataService dataService;
    private int clttype = 1;
    private JTable table;
    private DataSearchPara para;
    private boolean admin;

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    private Check2SPinner c1;
    private Check2SPinner c2;

    @Override
    public DataManegePanel init() {
        setLayout(new BorderLayout());
        initcenter();
        initializeTable();
        return this;
    }

    private JButton search;
    private JButton delete;
    private JButton clear;
    private CardLayout card;
    private JPanel center;

    private void initcenter() {
        table = new JTable(tableModel);
        JScrollPane jScrollPane = new JScrollPane(table);
        card = new CardLayout();
        center = new JPanel(card);
        JPanel bottom = new JPanel();
        Calendar calendar = Calendar.getInstance();
        c2 = new Check2SPinner(false, calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        c1 = new Check2SPinner(false, calendar.getTime());
        bottom.add(c1);
        bottom.add(c2);
        search = new JButton("查询");
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getSearchPara();
                refreashData();
            }
        });
        bottom.add(search);
        delete = new JButton("删除");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector<SF6DataBean> datas = getSelectInfos();
                if (datas.size() <= 0) {
                    errorMessage("请选择要删除的信息");
                    return;
                }
                Map<SF6UnitBean, List<Date>> datamap = getDateMap(datas);
                if (dataService.deleteData(datamap)) {
                    successMessage("删除成功");
                    search.doClick();
                } else {
                    falseMessage("删除失败");
                }

            }
        });

        clear = new JButton("清空");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector<SF6DataBean> datas = getAllInfos();
                if (datas.size() <= 0) {
                    errorMessage("当前表中无信息");
                    return;
                }
                Map<SF6UnitBean, List<Date>> datamap = getDateMap(datas);
                if (dataService.deleteData(datamap)) {
                    successMessage("清空成功");
                    tableModel.clearData();
                } else {
                    falseMessage("清空失败");
                }
            }
        });
        if (admin) {
            bottom.add(delete);
            bottom.add(clear);
        }
        JPanel waitpanel = new JPanel(new BorderLayout());
        waitpanel.add(new JLabel(factorys.getIconFactory().getIcon("progress")));
        center.add(jScrollPane, "table");
        center.add(waitpanel, "wait");
        this.add(center, BorderLayout.CENTER);
        this.add(bottom, BorderLayout.SOUTH);
    }

    private Vector<SF6DataBean> getSelectInfos() {
        int[] rows = table.getSelectedRows();
        Vector<SF6DataBean> datas = new Vector<>();
        for (int row : rows) {
            SF6DataBean data = new SF6DataBean();
            data.resolveTotalInfoTable(table, row);
            datas.add(data);
        }
        return datas;
    }

    private Vector<SF6DataBean> getAllInfos() {
        Vector<SF6DataBean> datas = new Vector<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            SF6DataBean data = new SF6DataBean();
            data.resolveTotalInfoTable(table, i);
            datas.add(data);
        }
        return datas;
    }

    private void addData(List<SF6DataBean> datas) {
        List<Vector<Object>> vectors = new ArrayList<>();
        for (SF6DataBean data : datas) {
            vectors.add(data.getDataTotalCol());
        }
        tableModel.addDatas(vectors);
    }

    private Map<SF6UnitBean, List<Date>> getDateMap(Vector<SF6DataBean> datas) {
        Map<SF6UnitBean, List<Date>> datamap = new HashMap<>();
        for (SF6DataBean data : datas) {
            SF6UnitBean unit = (SF6UnitBean) unitService.getUnitByNumber(clttype, data.getUnit_num());
            if (unit != null && datamap.containsKey(unit)) {
                datamap.get(unit).add(data.getInserttime());
            } else {
                List<Date> dates = new ArrayList<>();
                dates.add(data.getInserttime());
                datamap.put(unit, dates);
            }
        }
        return datamap;
    }

    private void getSearchPara() {
        // 物理量
        this.para = new DataSearchPara();
        para.setClttype(clttype);
        Date t1 = c1.getTime();
        Date t2 = c2.getTime();
        if (t1 != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(t1);
            c1.set(Calendar.HOUR_OF_DAY, 0);
            c1.set(Calendar.MINUTE, 0);
            c1.set(Calendar.SECOND, 0);
            c1.set(Calendar.MILLISECOND, 0);
            para.setT1(c1.getTime());
//            System.out.println(t1);
        }
        if (t2 != null) {
            Calendar c2 = Calendar.getInstance();
            c2.setTime(t2);
            c2.set(Calendar.HOUR_OF_DAY, 23);
            c2.set(Calendar.MINUTE, 59);
            c2.set(Calendar.SECOND, 59);
            c2.set(Calendar.MILLISECOND, 999);
            para.setT2(c2.getTime());
//            System.out.println(t2);
        }
        /*if (t1 == null || t2 == null) {
            if (t1 != null) {
                c1.setTime(t1);
                c2.setTime(t1);
                c2.add(Calendar.DAY_OF_MONTH, 1);
            } else if (t2 != null) {
                c1.setTime(t2);
                c2.setTime(t2);
                c1.add(Calendar.DAY_OF_MONTH, -1);
            } else {
                c1.add(Calendar.DAY_OF_MONTH, -1);
            }
        } else {
            if (t1.after(t2)) {
                errorMessage("起始时间应位于结束时间之前");
                para = null;
                return;
            }
            c1.setTime(t1);
            c2.setTime(t2);
        }*/


    }

    @Resource
    private TCR tcr;

    private void initializeTable() {
        tcr.initializeTable(table);
    }

    @Override
    public void refreashData() {
        if (para == null) {
            return;
        }
        card.show(center, "wait");
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<SF6DataBean> dataList = dataService.getData(para);
                addData(dataList);
                card.show(center, "table");
            }
        }).start();
    }
}
