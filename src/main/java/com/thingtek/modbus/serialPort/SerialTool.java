package com.thingtek.modbus.serialPort;

import org.springframework.stereotype.Component;

import javax.comm.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.Vector;


/**
 * 串口服务类，提供打开、关闭串口，读取、发送串口数据等服务（采用单例设计模式）
 *
 * @author zhong
 */
@Component
public class SerialTool {

    public SerialTool() {
        try {
            String driverName = "com.sun.comm.Win32Driver"; // or get as a JNLP property
            CommDriver commDriver = (CommDriver) Class.forName(driverName).newInstance();
            commDriver.initialize();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查找所有可用端口
     *
     * @return 可用端口名称列表
     */
    public Vector<String> findPort() {

        //获得当前所有可用串口
        Enumeration en = CommPortIdentifier.getPortIdentifiers();
        Vector<String> portNameList = new Vector<>();
        CommPortIdentifier portId;
        //将可用串口名添加到List并返回该List
        while (en.hasMoreElements()) {
            portId = (CommPortIdentifier) en.nextElement();
//            System.out.println(portId);
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (!portNameList.contains(portId.getName())) {
                    portNameList.add(portId.getName());
                }
            }
        }
//        System.out.println(portNameList);
        return portNameList;
    }

    /**
     * 打开串口
     *
     * @param portName 端口名称
     * @param baudrate 波特率
     * @return 串口对象
     */
    public SerialPort openPort(String portName, int baudrate) {
        try {
            //通过端口名识别端口
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            //打开端口，并给端口名字和一个timeout（打开操作的超时时间）
            CommPort commPort = portIdentifier.open(portName, 2000);
            //判断是不是串口
            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                try {
                    //设置一下串口的波特率等参数
                    serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                } catch (UnsupportedCommOperationException e) {
                    e.printStackTrace();

                }
                return serialPort;
            }
            //不是串口

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭串口
     *
     * @param serialPort 待关闭的串口对象
     */

    public void closePort(SerialPort serialPort) {
        System.out.println("关闭串口");
        if (serialPort != null) {
            serialPort.close();
        }
        System.out.println("关闭串口成功");
    }

    /**
     * 往串口发送数据
     *
     * @param serialPort 串口对象
     * @param order      待发送数据
     */
    public void sendToPort(SerialPort serialPort, byte[] order) {
        OutputStream out = null;
        try {
            out = serialPort.getOutputStream();
            out.write(order);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从串口读取数据
     *
     * @param serialPort 当前已建立连接的SerialPort对象
     * @return 读取到的数据
     */
    public byte[] readFromPort(SerialPort serialPort) throws IOException {

        InputStream in = null;
        byte[] bytes = null;
        try {

            in = serialPort.getInputStream();
            int bufflenth = in.available();        //获取buffer里的数据长度
            while (bufflenth != 0) {
                bytes = new byte[bufflenth];    //初始化byte数组为buffer中数据的长度
                in.read(bytes);
                bufflenth = in.available();
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return bytes;

    }

    /**
     * 添加监听器
     *
     * @param port     串口对象
     * @param listener 串口监听器
     */
    public void addListener(SerialPort port, SerialPortEventListener listener) throws TooManyListenersException {

        //给串口添加监听器
        port.addEventListener(listener);
        //设置当有数据到达时唤醒监听接收线程
        port.notifyOnDataAvailable(true);
        //设置当通信中断时唤醒中断线程
        port.notifyOnBreakInterrupt(true);


    }


}
