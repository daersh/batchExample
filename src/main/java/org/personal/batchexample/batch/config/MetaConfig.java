package org.personal.batchexample.batch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class MetaConfig {

    // 데이터소스 빈이 두개가 되면 충돌 발생하기 떄문에 하나는 primary 세팅 해둬야함
    @Primary
    @Bean
    //datasource-meta안에 4가지 정보를 가져옴
    @ConfigurationProperties(prefix = "spring.datasource-meta")
    public DataSource metaDBSource(){
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager metaTransactionManager(){
            return new DataSourceTransactionManager(metaDBSource());
    }
}
