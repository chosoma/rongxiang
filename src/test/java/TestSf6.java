import com.thingtek.beanServiceDao.data.entity.SF6DataBean;
import com.thingtek.beanServiceDao.data.service.SF6DataService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Calendar;

public class TestSf6 {
    private String config = "appcontext/applicationContext.xml";
    private ApplicationContext ac = new ClassPathXmlApplicationContext(config);
    SF6DataService service = ac.getBean(SF6DataService.class);

    @Test
    public void save() {
        SF6DataBean[] data = new SF6DataBean[21];
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -1);
        for (short i = 0; i < data.length; i++) {
            data[i] = new SF6DataBean();
            data[i].setUnit_num((byte) (i + 1));
            data[i].setMidu((float) i);
            data[i].setYali((float) i);
            data[i].setMidu((float) i);
            data[i].setInserttime(c.getTime());
        }
        System.out.println(service.saveData(data));
    }

    @Test
    public void getlast() {
        System.out.println(service.getLatestDatas());
    }

}
