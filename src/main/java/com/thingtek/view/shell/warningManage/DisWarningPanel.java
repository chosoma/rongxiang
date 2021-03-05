package com.thingtek.view.shell.warningManage;

import com.thingtek.beanServiceDao.data.entity.DisDataBean;
import com.thingtek.beanServiceDao.data.service.DisDataService;
import com.thingtek.beanServiceDao.unit.entity.DisUnitBean;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.socket.data.entity.DataSearchPara;
import com.thingtek.view.component.panel.Check2SPinner;
import com.thingtek.view.component.tablecellrander.TCR;
import com.thingtek.view.component.tablemodel.DisDataTableModel;
import com.thingtek.view.shell.BasePanel;
import com.thingtek.view.shell.DataPanel;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * 图标 时间选择 曲线框
 */
public class DisWarningPanel extends BasePanel implements DataPanel {
    private int clttype = 4;
    @Resource
    private UnitService unitService;
    @Resource
    private DisDataService dataService;
    @Resource
    private DisDataTableModel tablemodel;
    private JTable table;
    private DataSearchPara para;

    private Check2SPinner c1;
    private Check2SPinner c2;
    private JSplitPane center;
    private JPanel left;
    private LinePanel linePanel;

    private boolean admin;

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public DisWarningPanel init() {
        this.setLayout(new BorderLayout());
        center = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        center.setDividerLocation(500);//设置分隔条位置
//        center.setLayout(new BorderLayout());
//        center.add(linePanel, BorderLayout.CENTER);
        initleft();
        initright();
        initializeTable();
        center.setLeftComponent(left);
        center.setRightComponent(right);
        this.add(center, BorderLayout.CENTER);
        return this;
    }

    private JButton search;
    private JButton delete;
    private JButton clear;

    private MouseAdapter tablemouseadaper;

    private void initleft() {
        table = new JTable(tablemodel);
        tablemouseadaper = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showline();
            }
        };
        table.addMouseListener(tablemouseadaper);
        JScrollPane scrollPane = new JScrollPane(table);
        left = new JPanel(new BorderLayout());
        left.add(scrollPane, BorderLayout.CENTER);
        JPanel leftbottom = new JPanel();
        Calendar calendar = Calendar.getInstance();
        c2 = new Check2SPinner(false, calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        c1 = new Check2SPinner(false, calendar.getTime());
        leftbottom.add(c1);
        leftbottom.add(c2);
        search = new JButton("查询");
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getSearchPara();
                refreashData();
            }
        });
        leftbottom.add(search);
        delete = new JButton("删除");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector<DisDataBean> datas = getSelectInfos();
                if (datas.size() <= 0) {
                    errorMessage("请选择要删除的信息");
                    return;
                }
                Map<DisUnitBean, List<Date>> datamap = getDateMap(datas);
                if (dataService.deleteData(datamap)) {
                    successMessage("删除成功");
                    search.doClick();
                    card.show(right, "null");
                } else {
                    falseMessage("删除失败");
                }
            }
        });

        clear = new JButton("清空");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector<DisDataBean> datas = getAllInfos();
                if (datas.size() <= 0) {
                    errorMessage("当前表中无数据");
                    return;
                }
                Map<DisUnitBean, List<Date>> datamap = getDateMap(datas);
                if (dataService.deleteData(datamap)) {
                    successMessage("清空成功");
                    tablemodel.clearData();
                    card.show(right, "null");
                } else {
                    falseMessage("清空失败");
                }
            }
        });
        if (admin) {
            leftbottom.add(delete);
            leftbottom.add(clear);
        }
        left.add(leftbottom, BorderLayout.SOUTH);
    }

    private Thread thread;

    private void showline() {
        if (thread != null) {
            thread.interrupt();
        }
        card.show(right, "wait");
        table.removeMouseListener(tablemouseadaper);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DisDataBean data = getSelectInfo();
                DisDataBean lineData = dataService.getData(data);
                linePanel.addData(lineData);
                card.show(right, "line");
                table.addMouseListener(tablemouseadaper);
            }
        });
        thread.start();
    }

    private void addData(List<DisDataBean> datas) {
        Vector<Vector<Object>> vectors = new Vector<>();
        for (DisDataBean data : datas) {
            vectors.add(data.getDataTotalCol());
        }
        tablemodel.addDatas(vectors);
    }

    private DisDataBean getSelectInfo() {
        DisDataBean data = new DisDataBean();
        data.resolveTotalInfoTable(table, table.getSelectedRow());
        return data;
    }

    private Vector<DisDataBean> getSelectInfos() {
        int[] rows = table.getSelectedRows();
        Vector<DisDataBean> datas = new Vector<>();
        for (int row : rows) {
            DisDataBean data = new DisDataBean();
            data.resolveTotalInfoTable(table, row);
            datas.add(data);
        }
        return datas;
    }

    private Vector<DisDataBean> getAllInfos() {
        Vector<DisDataBean> datas = new Vector<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            DisDataBean data = new DisDataBean();
            data.resolveTotalInfoTable(table, i);
            datas.add(data);
        }
        return datas;
    }

    private Map<DisUnitBean, List<Date>> getDateMap(Vector<DisDataBean> datas) {
        Map<DisUnitBean, List<Date>> datamap = new HashMap<>();
        for (DisDataBean data : datas) {
            DisUnitBean unit = (DisUnitBean) unitService.getUnitByNumber(clttype, data.getUnit_num());
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

    private int[] getTableSelect() {
        return null;
    }

    private JPanel right;
    private CardLayout card;

    private void initright() {
        card = new CardLayout();
        JPanel nullpanel = new JPanel();
        right = new JPanel(card);
        right.add(nullpanel, "null");
        linePanel = new LinePanel();
        right.add(linePanel, "line");
        JPanel waitpanel = new JPanel(new BorderLayout());
        waitpanel.add(new JLabel(factorys.getIconFactory().getIcon("progress")));
        right.add(waitpanel, "wait");
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
        if(para == null){
            return;
        }
        List<DisDataBean> dataList = dataService.getDataInfo(para);
        addData(dataList);
    }
}
