import com.thingtek.beanServiceDao.clt.service.CltService;
import com.thingtek.modbus.serialPort.SerialTool;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSerialTool {
    private String config = "appcontext/applicationContext.xml";
    private ApplicationContext ac = new ClassPathXmlApplicationContext(config);
    private SerialTool serialTool = ac.getBean(SerialTool.class);
    @Test
    public void testSerial() {
        System.out.println(serialTool.findPort());
    }
}
