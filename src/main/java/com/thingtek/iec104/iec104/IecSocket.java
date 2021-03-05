package com.thingtek.iec104.iec104;


import com.thingtek.beanServiceDao.base.service.BaseService;
import com.thingtek.config.PortConfig;
import com.thingtek.iec104.agreement.IECAgreementConfig;
import com.thingtek.iec104.entity.BaseS2G;
import com.thingtek.iec104.entity.S2GStop;
import com.thingtek.iec104.entity.S2GZong;
import com.thingtek.view.shell.debugs.Debugs;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

@Component
public class IecSocket extends BaseService {

    private InputStream in;
    private OutputStream out;
    @Resource
    private IECAgreementConfig agreement;

    public IecSocket() {
//        writelist = new ArrayList<>();
        Init.start();

    }

    @Resource
    private PortConfig portConfig;
    private ServerSocket serverSocket;

    public void init() {
        receiveSeqNum = 0;
        sendSeqNum = 0;
        try {
            serverSocket = new ServerSocket(portConfig.getIecport());
            while (true) {
                Socket socket = serverSocket.accept();
                IecSocketThread iecSocketThread = new IecSocketThread();
                iecSocketThread.setAgreement(agreement);
                iecSocketThread.setDebugShow(debugShow);
                iecSocketThread.setPortConfig(portConfig);
                iecSocketThread.setSocket(socket);
                Thread thread = new Thread(iecSocketThread);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    // 定义的是本地远程命令发送帧的接收和发送序号
    public static int receiveSeqNum = 0; //接收序号
    public static int sendSeqNum = 0; // 发送序号，每发送一个后需+1

    @Resource
    private Debugs debugShow;

    private byte[] cache;
    private Socket socket;

   /* @Override
    public void run() {
        try {
//            socket = new Socket(portConfig.getIecip(), portConfig.getIecport());
//            socket = serverSocket.accept();
            receiveSeqNum = 0;
            sendSeqNum = 0;
            in = socket.getInputStream();
            out = socket.getOutputStream();
            byte[] b = new byte[1024 * 1024];
            int num;
            while ((num = in.read(b)) != -1) {
                Date time = Calendar.getInstance().getTime();
                debugShow.rec(b, num, time, " " + portConfig.getIecip());

                byte[] bytes = new byte[num];
                System.arraycopy(b, 0, bytes, 0, bytes.length);
                if (cache == null || cache.length == 0) {
                    cache = bytes;
                } else {
                    int length = cache.length;
                    cache = Arrays.copyOf(cache, length + num);
                    System.arraycopy(bytes, 0, cache, length, bytes.length);
                }
                debugShow.rec(cache, cache.length, time, " cache:" + portConfig.getIecip());
                resolvecache();
            }
            System.out.println("链接关闭");
        } catch (Exception e) {
            log(e);
            cache = null;
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                log(ex);
            }
            run();
        } finally {
            close();
        }
        run();
    }

    private void resolvecache() {
        int headoff = agreement.getheadindex(cache);
        if (headoff < 0) {
            return;
        }
        if (cache.length - headoff < 2) {
            return;
        }
        int datalengthoff = agreement.getDatalengthoff();
        int datalength = cache[headoff + datalengthoff];
        if (cache.length - headoff < datalength + 2) {
            return;
        }

        byte[] bytes = new byte[datalength];
        byte order = bytes[0];
        System.arraycopy(cache, headoff + 2, bytes, 0, bytes.length);
        if (datalength < agreement.getSUzhen()) {
            //不知道是啥
            return;
        } else if (datalength == agreement.getSUzhen()) {
            //s帧 和 u帧
            //类型标示在长度后
            order = bytes[agreement.getSuorderoff()];
        } else {
            //i帧
            //类型标示在序列后
            order = bytes[agreement.getIorderoff()];
            receiveSeqNum += 2;
//            System.out.println("接收序号:"+receiveSeqNum);
            int sendSeqNum = (bytes[0] & 0xff) | ((bytes[1] & 0xff) << 8);
            int receiveSeqNum = (bytes[2] & 0xff) | ((bytes[3] & 0xff) << 8);
//            System.out.println(sendSeqNum);
//            System.out.println(receiveSeqNum);
        }
        BaseS2G s2g = agreement.gets2g(order);
        s2g.setBytes(bytes);
        s2g.setIecSocket(this);
        s2g.resolve();
        byte[] result = s2g.getResult();
        if (!(result == null || result.length == 0)) {
            byte[] send = getsend(result);
            send(send);
            if (s2g instanceof S2GZong) {
                sendwritelist();
            }
            if (s2g instanceof S2GStop) {
                close();
                return;
            }
        } else {
            sendwritelist();
        }


        byte[] other = new byte[cache.length - headoff - datalength - 2];
        System.arraycopy(cache, headoff + datalength + 2, other, 0, other.length);
        cache = other;
        resolvecache();
    }

    private List<byte[]> writelist;

    public void addwrite(byte[] bytes) {
        writelist.add(bytes);
    }

    public void clear() {
        writelist.clear();
    }

    public void send(byte[] bytes) {
        try {
            out.write(bytes);
            debugShow.send(bytes, bytes.length, " " + portConfig.getIecip());
        } catch (IOException e) {
            log(e);
        }
    }

    public void sendwritelist() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (writelist.size() == 0) {
            return;
        }
        byte[] send = writelist.remove(0);
        byte[] bytes = getsend(send);
        send(bytes);
        sendwritelist();
    }

    public byte[] getsend(byte[] bytes) {
        byte[] sends;
        if (bytes.length > agreement.getSUzhen()) {
//            System.out.println("发送序号:"+sendSeqNum);
            bytes[0] = (byte) sendSeqNum;
            bytes[1] = (byte) (sendSeqNum >> 8);
            bytes[2] = (byte) receiveSeqNum;
            bytes[3] = (byte) (receiveSeqNum >> 8);
            sendSeqNum += 2;
            sends = new byte[bytes.length + 2];
            System.arraycopy(bytes, 0, sends, 2, bytes.length);
        } else {
            sends = new byte[bytes.length + 2];
            System.arraycopy(bytes, 0, sends, 2, bytes.length);
        }
        sends[agreement.getHeadoff()] = agreement.getHead();
        sends[agreement.getDatalengthoff()] = (byte) bytes.length;
        return sends;
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
            writelist.clear();
            cache = null;
        } catch (IOException e) {
            log(e);
        }
    }
*/
}
