import com.thingtek.beanServiceDao.warn.entity.WarnBean;
import com.thingtek.beanServiceDao.warn.service.WarnService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TestWarn {
    private String config = "appcontext/applicationContext.xml";
    private ApplicationContext ac = new ClassPathXmlApplicationContext(config);
    WarnService warnService = ac.getBean(WarnService.class);
    @Test
    public void save(){
        List<WarnBean> warnBeans = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            WarnBean warnBean = new WarnBean();
            warnBean.setClt_type(1);
            warnBean.setUnit_num((byte) i);
            warnBean.setWarn_info(String.valueOf(i));
            warnBean.setInserttime(Calendar.getInstance().getTime());
            warnBeans.add(warnBean);
        }
        warnService.save(warnBeans.toArray(new WarnBean[0]));
    }
}
