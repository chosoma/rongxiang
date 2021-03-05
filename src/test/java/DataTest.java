import com.thingtek.beanServiceDao.data.entity.DisDataBean;
import com.thingtek.beanServiceDao.data.service.DisDataService;
import com.thingtek.socket.data.entity.DataSearchPara;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class DataTest {
    /* public static void main(String[] args) {
         String str = "";
         for (int i = 0; i < 10000; i++) {
             System.out.println("  `value"+i+"` int(6) DEFAULT NULL,");
         }
     }*/
    public static void main(String[] args) {
        String str = "";
        for (int i = 0; i < 1000; i++) {
            System.out.println(" value" + i + ",");
        }
    }

    private String config = "appcontext/applicationContext.xml";
    private ApplicationContext ac = new ClassPathXmlApplicationContext(config);

    @Test
    public void testSave() throws InterruptedException {
//        for (int i = 0; i < 10; i++) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
        long start = System.currentTimeMillis();
        DisDataBean dataBean = new DisDataBean();
        int[] ints = new int[60000];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = (int) (Math.random() * 100 + 100);
        }
        dataBean.setData(ints);
        dataBean.setUnit_num((byte) 1);
        Calendar c = Calendar.getInstance();
        dataBean.setInserttime(c.getTime());
        saveDataXml(dataBean);
//                    DataService dataService = ac.getBean(DataService.class);
//                    dataService.saveData(dataBean);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
//                }
//            }).start();
//        }
//        Thread.sleep(100000);

    }

    @Test
    public void testSaveTotal() {
        Calendar c = Calendar.getInstance();
        DisDataBean dataBean = new DisDataBean();
        for (int k = 0; k < 200; k++) {

        DisDataBean[] dataBeans = new DisDataBean[100];
        for (int i = 0; i < dataBeans.length; i++) {
            dataBeans[i] = new DisDataBean();
            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < 60000; j++) {
                stringBuilder.append((int) (Math.random() * 10000 + 10000));
                stringBuilder.append(",");
            }
            dataBeans[i].setDatastring(stringBuilder.toString());
            dataBeans[i].setUnit_num((byte) 1);
            c.add(Calendar.SECOND, 1);
            dataBeans[i].setDatacount(i);
            dataBeans[i].setInserttime(c.getTime());
//            System.out.println(c.getTimeInMillis());
        }
        long start = System.currentTimeMillis();
//        JASONObject  jason = new JASONObject();
        DisDataService dataService = ac.getBean(DisDataService.class);
        dataService.saveData(dataBeans);
//        saveDataXml(dataBeans);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        }

    }

    @Test
    public void testFindInfo() {
//        long start = System.currentTimeMillis();
//        DataService dataService = ac.getBean(DataService.class);
//        DataSearchPara dsp = new DataSearchPara();
//        Calendar c = Calendar.getInstance();
//        c.add(Calendar.DAY_OF_MONTH, -1);
//        dsp.setT1(c.getTime());
//        dsp.setT2(Calendar.getInstance().getTime());
//        dsp.setUnit_num((byte) 1);
//        List<DisDataBean> datas = dataService.getData(dsp);
//        System.out.println(datas);
//        long end = System.currentTimeMillis();
//        System.out.println(end - start);
    }

    @Test
    public void testFindTotal() {
        long start = System.currentTimeMillis();
        DisDataBean data = new DisDataBean();
        data.setUnit_num((byte) 1);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 9);
        c.set(Calendar.MINUTE, 37);
        c.set(Calendar.SECOND, 3);
        c.set(Calendar.YEAR, 2019);
        c.set(Calendar.MONTH, Calendar.SEPTEMBER);
        c.set(Calendar.DAY_OF_MONTH, 16);
        c.set(Calendar.MILLISECOND,0);
        data.setInserttime(c.getTime());

        DisDataService dataService = ac.getBean(DisDataService.class);
        DataSearchPara dsp = new DataSearchPara();
        dsp.setClttype(4);
        dataService.getDataInfo(dsp);
        DisDataBean resolve = dataService.getData(data);
        System.out.println(resolve);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    @Test
    public void testMathRadom() {
        System.out.println(Math.random());
    }


    private void saveDataXml(DisDataBean... dataBeans) {
        for (DisDataBean databean : dataBeans) {
            String filename = "data/" + databean.getUnit_num() + "_" + databean.getInserttime().getTime() + ".xml";
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement("property");
            Element unitnum = root.addElement("unit_num");
            unitnum.addText(String.valueOf(databean.getUnit_num()));
            Element inserttime = root.addElement("inserttime");
            inserttime.addText(String.valueOf(databean.getInserttime().getTime()));
            int[] values = databean.getData();
            StringBuilder stringBuilder = new StringBuilder();
            Element valueelemenet = root.addElement("value");
            for (int i = 0; i < values.length; i++) {
                stringBuilder.append(values[i]);
                stringBuilder.append(",");
            }
            valueelemenet.addText(stringBuilder.toString());
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            try {
                XMLWriter writer = new XMLWriter(new FileOutputStream(filename), format);
                writer.write(document);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testReadDataXml() {
        long start = System.currentTimeMillis();
        readDataXml();
        System.out.println(System.currentTimeMillis() - start);
    }

    private void readDataXml() {
        String filename = "data/" + "1_1562822982158.xml";
        try {
            DisDataBean dataBean = new DisDataBean();
            SAXReader reader = new SAXReader();
            Document document = reader.read(filename);
            Element root = document.getRootElement();
            Element unitnum = root.element("unit_num");
            dataBean.setUnit_num(Byte.valueOf(unitnum.getText()));
            unitnum.getText();
            Element value = root.element("value");
            String str = value.getText();
            String[] strs = str.split(",");
//            System.out.println(strs.length);
            int[] values = new int[strs.length];
            for (int i = 0; i < strs.length; i++) {
                values[i] = Integer.parseInt(strs[i]);
            }
            dataBean.setData(values);
            Element inserttime = root.element("inserttime");
            long inserttimelong = Long.parseLong(inserttime.getText());
            dataBean.setInserttime(new Date(inserttimelong));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveDataProperties(DisDataBean dataBean) {
        Properties properties = new Properties();
        properties.put("unit_num", dataBean.getUnit_num());
        properties.put("inserttime", dataBean.getInserttime().getTime());
        int[] values = dataBean.getData();
        for (int i = 0; i < values.length; i++) {
//            properties.put()

        }
    }

}
