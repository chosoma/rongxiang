import com.thingtek.beanServiceDao.clt.service.CltService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestCollet {

    private String config = "appcontext/applicationContext.xml";
    private ApplicationContext ac = new ClassPathXmlApplicationContext(config);
    private CltService service = ac.getBean(CltService.class);

    @Test
    public void testFindAll() {
        System.out.println(service.getAll());
    }


}
