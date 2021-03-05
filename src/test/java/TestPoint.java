import com.thingtek.beanServiceDao.point.service.PointService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestPoint {
    private String config = "appcontext/applicationContext.xml";
    private ApplicationContext ac = new ClassPathXmlApplicationContext(config);
    private PointService service = ac.getBean(PointService.class);
    @Test
    public void findAll(){
        System.out.println(service.getPoints(4));
    }
}
