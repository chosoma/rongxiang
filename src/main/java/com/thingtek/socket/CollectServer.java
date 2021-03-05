package com.thingtek.socket;

import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.socket.agreement.SocketAgreement;
import com.thingtek.config.PortConfig;
import com.thingtek.view.shell.debugs.Debugs;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.UnitSetPanel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


@Component
public class CollectServer {

    private ServerSocket ss;
    private List<CollectSocket> listST = new ArrayList<CollectSocket>();
    private List<CollectSocketUp> upList = new ArrayList<>();

    @Resource
    private PortConfig portConfig;
    @Resource
    private UnitService unitService;
    @Resource
    private UnitSetPanel unitSetPanel;

    private CollectServer() {

    }

    @Resource
    private Debugs debugs;
    @Resource
    private SocketAgreement agreement;

    /**
     * 启动服务
     */
    public void openConnection() {
        int localPort = portConfig.getPort();
        try {
            ss = new ServerSocket(localPort);
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(null, "程序已经启动");
            System.exit(0);
            e1.printStackTrace();
            return;
        }
        listST.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Socket s = ss.accept();
                        String addr = s.getInetAddress().getHostAddress();
                         /*if(addr.equals("192.168.1.66")){
                            CollectSocketUp socket = new CollectSocketUp(s);
                            System.out.println(s.getInetAddress().getHostAddress());
                            socket.setAgreement(agreement);
                            socket.setDebugShow(debugs);
                            socket.setServer(CollectServer.this);
                            socket.setUnitService(unitService);
                            socket.setUnitSetPanel(unitSetPanel);
                            Thread thread = new Thread(socket);
                            thread.start();
                            continue;
                        }*/
                        CollectSocket socket = new CollectSocket(s);
                        socket.setAgreement(agreement);
                        socket.setDebugShow(debugs);
                        socket.setServer(CollectServer.this);
                        socket.setUnitService(unitService);
                        socket.setUnitSetPanel(unitSetPanel);
                        Thread thread = new Thread(socket);
                        thread.start();
                    }
                } catch (IOException e) {
                    // e.printStackTrace();
                } finally {
                    // 关闭serverSocket
                    if (ss != null && !ss.isClosed()) {
                        try {
                            ss.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    //
//    /**
//     * 关闭服务
//     */
    public void closeConnection() {
        try {
            if (ss != null) {
                ss.close();
            }
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(null, "程序已启动");
            System.exit(0);
            e1.printStackTrace();
        }
        // 关闭socket
        for (int i = listST.size() - 1; i >= 0; i--) {
            CollectSocket st = listST.get(i);
            try {
                st.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        listST.clear();
    }

    // 移除Socket
    synchronized void removeSocket(CollectSocket st) {
        listST.remove(st);
        checkUseful();
    }
    synchronized void removeSocket(CollectSocketUp socketUp){
        upList.remove(socketUp);
    }

    // 添加Socket，并移除关闭和未连接的
    synchronized void addSocket(CollectSocket st) {
        listST.add(st);
        checkUseful();
    }
    synchronized void addSocket(CollectSocketUp st) {
        upList.add(st);
        checkUseful();
    }
    public CollectSocket getSocket(String ip) {
        for (CollectSocket socket : listST) {
            if (socket.getSocket().getInetAddress().getHostAddress().equals(ip)) {
                return socket;
            }
        }
        return null;
    }

    private void checkUseful() {
        for (int i = 0; i < listST.size(); i++) {
            CollectSocket s = listST.get(i);
            if (s.isClosed() || !s.isConnected()) {
                listST.remove(i);
                i--;
            }
        }
        for (int i = 0; i < upList.size(); i++) {
            CollectSocketUp s = upList.get(i);
            if (s.isClosed() || !s.isConnected()) {
                listST.remove(i);
                i--;
            }
        }
    }

    /**
     * 设置报警
     *//*
    public void setAlarm(NetBean net, byte time) {
        for (int i = 0; i < listST.size(); i++) {
            CollectSocket s = listST.get(i);
            if (s.isUseful()) {
                if (net.getType() == s.getNetType() && net.getNumber() == s.getNetId()) {
                    try {
                        s.setAlarm(time);
                    } catch (IOException e) {
                        // e.printStackTrace();
                        listST.remove(i);
                        i--;
                    }
                }
            } else {
                listST.remove(i);
                i--;
            }
        }

    }*/
    public boolean isNullLinked() {
        return listST.size() == 0;
    }


    /*
        server-client设置采集周期
     */
    /*public void applyUnitOffline(byte unitType, Byte unitNumber, byte jg) {
        if (unitNumber == null) {
            Vector<DisUnitBean> beans = UnitService.getUnitBeans(unitType);
            if (beans.size() <= 0) {
                return;
            }
            for (DisUnitBean unitBean : beans) {
                applyUnitOffline(unitType, unitBean.getNumber(), jg);
            }
        } else {
            DisUnitBean unitBean = UnitService.getUnitBean(unitType, unitNumber);
            for (int i = 0; i < listST.size(); i++) {
                CollectSocket s = listST.get(i);
                if (s.isUseful()) {
//                    if (unitBean != null && unitBean.getGatewaytype() == s.getNetType() && unitBean.getGatewaynumber() == s.getNetId()) {
                    try {
                        s.setJg(unitBean, jg);
                        // break;
                    } catch (IOException e) {
                        // e.printStackTrace();
                        listST.remove(i);
                        i--;
                    }
//                    }
                } else {
                    listST.remove(i);
                    i--;
                }
            }
        }
    }*/


}
