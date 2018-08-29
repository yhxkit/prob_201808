package config;
// 뷰 컨트롤러 설정


import com.test.prob.controller.ListController;
import com.test.prob.model.ListDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ControllerConfig extends WebMvcConfigurerAdapter {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ListDao.class);

    @Override //뷰 컨트롤러 설정은 이 메서드 오버라이드로...
    public void addViewControllers(ViewControllerRegistry registry) {
//        super.addViewControllers(registry);

        log.info("컨트롤러~");

        //registry.addViewController("/").setViewName("home");

    }



//    @Bean
//    public ListController listController (){
////여기서 listdao set 안해줘도 자동 주입 되는거같음...
//     ListController listc = new ListController();
//     return listc;
//
//    }




}
