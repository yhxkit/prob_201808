package config;

import com.test.prob.model.ListDao;
import com.test.prob.model.ListDaoImpl;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;


//@EnableWebMvc //    <mvc:annotation-driven /> 과 동일... 서블릿 관리... //extends WebMvcConfigurerAdater 와 동일
@Configuration //아래의 @Bean들은 자체적으로 싱글톤 관리...
@PropertySource("classpath:/db.properties")
public class JavaConfig {

    @Autowired
    Environment env;

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ListDao.class);


    @Bean
public ListDao listdao() throws Exception{
        log.info("listdao 빈 생성 ");
    return new ListDaoImpl(sqlSession()); //그냥 Dao 인터페이스를 보낼수는 없나..? 주입은 Dao로 했는데..
}

@Bean
public SqlSession sqlSession() throws Exception{
    log.info("sqlsession 빈 생성 ");
    SqlSessionTemplate sqlSession = new SqlSessionTemplate(sqlSessionFactory());
    return sqlSession;
}


@Bean
public SqlSessionFactory sqlSessionFactory() throws Exception{

    log.info("sqlsessionfactory 빈 생성 ");

    SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
    sqlSessionFactory.setDataSource(dataSource());

    //매퍼
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    sqlSessionFactory.setMapperLocations(resolver.getResources("classpath:/mapper/listMapper.xml"));

    return sqlSessionFactory.getObject();
    }

    @Bean
    public SimpleDriverDataSource dataSource(){
        log.info("datasource 빈 생성 ");
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

//        try { //어떻게 하지,..??
//            dataSource.setDriver(Class.forName(env.getProperty("db.driver")));
//        }catch (Exception e){ log.info(e.toString());}

        dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
        dataSource.setUrl(env.getProperty("db.jdbcUrl"));
        dataSource.setUsername(env.getProperty("db.user"));
        dataSource.setPassword(env.getProperty("db.password"));
        return dataSource;
    }


 }
