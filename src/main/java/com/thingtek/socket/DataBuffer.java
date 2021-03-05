package com.thingtek.socket;

import com.thingtek.beanServiceDao.data.entity.DisDataBean;
import com.thingtek.beanServiceDao.data.service.DisDataService;
import com.thingtek.beanServiceDao.point.entity.PointBean;
import com.thingtek.beanServiceDao.point.service.PointService;
import com.thingtek.beanServiceDao.unit.entity.DisUnitBean;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.beanServiceDao.warn.entity.WarnBean;
import com.thingtek.beanServiceDao.warn.service.WarnService;
import com.thingtek.socket.RawData;
import com.thingtek.view.logo.LogoInfo;
import com.thingtek.view.shell.Shell;
import com.thingtek.view.shell.dataCollect.DataCollectPanel;
import com.thingtek.view.shell.dataCollect.base.BaseCollectPanel;
import com.thingtek.view.shell.homePage.HomePanel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class DataBuffer {
    //
//    @Resource
//    private DataFactory factory;
    // 字节数据缓冲区
    private int clttype = 4;
    private Map<Byte, Map<Integer, RawData[]>> buffer;//单元编号 , 大包编号  , 数据
    private List<RawData[]> wholebuffer;
    // 缓冲区添加数据和移除数据的线程锁
    private Object dataLock = new Object();
    // 数据处理线程
    private Thread dataThread;

    private Lock lock;
    private Condition con;
    @Resource
    private HomePanel homePanel;
    @Resource
    private DisDataService dataService;
    @Resource
    private UnitService unitService;
    @Resource
    private WarnService warnService;
    @Resource
    private PointService pointService;
    @Resource
    private Shell shell;

    public DataBuffer() {
        buffer = new HashMap<>();
        wholebuffer = new ArrayList<>();
        lock = new ReentrantLock();
        con = lock.newCondition();
        start();
    }

    /**
     * 将有效长度为length的数据添加到数据缓冲区
     *
     * @param //数据
     */
    public boolean receDatas(RawData rawData) {
        synchronized (wholebuffer) {
//            long start = System.currentTimeMillis();
            byte unitnum = rawData.getData().getUnit_num();
            int bigseq = rawData.getBigseq();
            int smallseq = rawData.getSmallseq();
            int totalsmallseq = rawData.getTotalsmallseq();
            if (buffer.containsKey(unitnum)) {
                Map<Integer, RawData[]> onebuf = buffer.get(unitnum);
//                System.out.println(onebuf);
                if (onebuf.containsKey(bigseq)) {
                    RawData[] raws = onebuf.get(bigseq);
//                    System.out.println(Arrays.toString(raws));
                    if (smallseq == 1 || raws[smallseq - 2] != null) {
//                        if (smallseq == 1) {
//                            raws[0].setTime(Calendar.getInstance().getTime());
//                        }
                        raws[smallseq - 1] = rawData;
                        if (smallseq == raws.length) {
                            wholebuffer.add(raws);
                            onebuf.remove(bigseq);
                        }
                    } else {
                        System.out.println("smallseq:" + smallseq + ",remove");
                        onebuf.remove(bigseq);
                        if (dataThread.getState() == Thread.State.WAITING) {
                            lock.lock();
                            con.signal();
                            lock.unlock();
                        }
                        return false;
                    }
                } else {
                    RawData[] raws = new RawData[totalsmallseq];
                    raws[smallseq - 1] = rawData;
                    onebuf.put(bigseq, raws);
                }
            } else {
                Map<Integer, RawData[]> onebuf = new HashMap<>();
                RawData[] raws = new RawData[totalsmallseq];
                raws[smallseq - 1] = rawData;
                onebuf.put(bigseq, raws);
                buffer.put(unitnum, onebuf);
            }
//            long end = System.currentTimeMillis();
//            System.out.println(end - start);
        }
        // 如果数据处理线程waiting中
        if (dataThread.getState() == Thread.State.WAITING) {
            lock.lock();
            con.signal();
            lock.unlock();
        }
        return true;
    }

    // 数据处理线程是否等待
    // public boolean isRunnable() {
    // return dataThread.getState() == Thread.State.RUNNABLE;
    // }

    /**
     * 数据处理 ：另起一个线程对数据缓冲区的数据进行处理
     */
    private void start() {
        if (dataThread != null) {
            if (dataThread.isAlive()) {
                return;
            }
        }
        dataThread = new Thread(new DataRunnable());
        dataThread.start();
    }

    private boolean alive = true;

    class DataRunnable implements Runnable {
        @Override
        public void run() {// 如果flag标志为true，则继续循环
            lock.lock();
            try {
                while (alive) {
                    // 判断缓冲区数据是否达到最小数据长度
                    if (wholebuffer.size() == 0) {
                        Thread.sleep(50);// 等待50毫秒
                        // 判断缓冲区数据是否达到最小数据长度,如果没有，则保存数据
                        if (wholebuffer.size() == 0) {
                            Thread.sleep(50);// 等待50毫秒
                            // 判断缓冲区数据是否达到最小数据长度,如果没有则线程休眠
                            if (wholebuffer.size() == 0) {
//                                factory.saveData();// 数据存储
                                con.await();//很关键
                            }
                            continue;
                        }
                    }

                    try {
                        // 单条数据内容长度
                        RawData[] datas = wholebuffer.remove(0);
                        StringBuilder stringBuilder = new StringBuilder();
                        for (RawData data1 : datas) {
//                                System.out.println("DataBuffer.150.data:" + data1);
                            DisDataBean data = data1.getData();
                            int[] ints = data.getData();
                            for (int anInt : ints) {
                                stringBuilder.append(anInt);
                                stringBuilder.append(",");
                            }
                        }
                        RawData rawData = datas[datas.length - 1];
                        DisDataBean data = rawData.getData();
                        DisUnitBean unit = (DisUnitBean) unitService.getUnitByNumber(clttype, data.getUnit_num());
                        data.setDatastring(stringBuilder.toString());
                        dataService.saveData(data);
                        int total = 0;
                        int max = 0;
                        int maxindex = 0;

                        int[] dataline = data.getData();

                        for (int i = 0; i < dataline.length; i++) {
                            total += dataline[i];
                            if (dataline[i] > max) {
                                max = dataline[i];
                                maxindex = i;
                            }
                        }
                        int jizhun = total / dataline.length;
                        int mintotal = max - jizhun;
                        int count = 1;
                        for (int i = maxindex + 1; i < maxindex + 500 && i < dataline.length; i++, count++) {
                            mintotal += (dataline[i] > jizhun ? dataline[i] - jizhun : 0);
                        }
                        double bili = mintotal / (max * count * 1.0);
                        System.out.print("基准值:" + jizhun);
                        System.out.print(",波形面积:" + mintotal);
                        System.out.print(",总面积:" + max * count);
                        System.out.println(",比例:" + bili);
                        PointBean pointBean = pointService.getPointByNum(clttype,unit.getPoint_num());
                        WarnBean warnBean = new WarnBean();
                        warnBean.setClt_type(clttype);
                        warnBean.setUnitBean(unit);
                        warnBean.setUnit_num(data.getUnit_num());
                        warnBean.setPhase(unit.getPhase());
                        warnBean.setPoint_name(pointBean.getPoint_name());
                        warnBean.setInserttime(data.getInserttime());
                        if (bili < 0.01 && data.getElectric() == 1) {
                            warnBean.setWarn_info(" ");
//                            shell.showWarn(clttype);
//                            addWarn(warnBean);
                            warnBean.setXianquan(1);
                        } else {
                            warnBean.setWarn_info(" ");
//                            addWarn(warnBean);
                            warnBean.setXianquan(0);
                        }
                        warnService.save(warnBean);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 数据线程出错
            } finally {
                lock.unlock();
            }
        }
    }

    @Resource
    private LogoInfo logoInfo;
    @Resource
    private DataCollectPanel collectPanel;

    private void addWarn(WarnBean warn) {
        for (BaseCollectPanel collectPanel : logoInfo.getCollectPanelMap().values()) {
            if (clttype == collectPanel.getClttype()) {
                collectPanel.addWarn(warn);
            }
        }
        collectPanel.addtablewarn(warn);
    }

    public void close() {
        alive = false;
        // 如果数据处理线程waiting中
        if (dataThread.getState() == Thread.State.WAITING) {
            lock.lock();
            con.signal();
            lock.unlock();
        }
    }

}
