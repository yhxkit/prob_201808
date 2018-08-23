package config;

import com.test.prob.model.ListDao;
import com.test.prob.model.ListDaoImpl;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration //아래의 @Bean들은 자체적으로 싱글톤 관리...
public class JavaConfig {

@Bean
public ListDao listdao(){
    return new ListDaoImpl(); //그냥 Dao 인터페이스를 보낼수는 없나..? 주입은 Dao로 했는데..
}

@Bean
public SqlSession sqlSession() throws Exception{
    SqlSessionTemplate sqlSession = new SqlSessionTemplate(sqlSessionFactory());
    return sqlSession;
}


@Bean
public SqlSessionFactory sqlSessionFactory() throws Exception{
    SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
    sqlSessionFactory.setDataSource(dataSource());

    //...? xml에선 매퍼도 같이 넣었는데?
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    sqlSessionFactory.setMapperLocations(resolver.getResources("classpath:/mapper/listMapper.xml"));

    return sqlSessionFactory.getObject();
}

@Bean
public SimpleDriverDataSource dataSource(){
    SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
    dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
    dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/testdb?useUnicode=true&characterEncoding=utf8");
    dataSource.setUsername("root");
    dataSource.setPassword("mysqlroot");
    return dataSource;
}








 }
