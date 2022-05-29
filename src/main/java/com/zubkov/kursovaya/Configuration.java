package com.zubkov.kursovaya;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.sql.DataSource;

@org.springframework.context.annotation.Configuration
public class Configuration
{
    @Bean
    public DataSource datasource() {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://localhost:5432/restaurant")
                .username("postgres")
                .password("6010")
                .build();
    }
}
