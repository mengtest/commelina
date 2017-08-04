import com.game.framework.netty.SpringStarter;
import com.google.common.base.Strings;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by @panyao on 2017/8/4.
 */
public class SpringBootTest {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/spring-*.xml");
        SpringStarter.start(context, (token) -> {
            if (Strings.isNullOrEmpty(token)) {
                return false;
            }
            return true;
        });
    }

}
