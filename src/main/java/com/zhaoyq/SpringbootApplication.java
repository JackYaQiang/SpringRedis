package com.zhaoyq;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ComponentScan(value = {"com.zhaoyq"},
        includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value ={org.springframework.stereotype.Controller.class,org.springframework.web.bind.annotation.ControllerAdvice.class}  ))
@EnableAutoConfiguration
@ImportResource({"classpath:beanContext.xml"})
public class SpringbootApplication {
    private final static Logger logger = Logger.getLogger(SpringbootApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
        logger.error("======================启动成功:");
        logger.error("======================启动成功:");
        logger.error("======================启动成功:");
        logger.error("======================启动成功:");
        logger.error("======================启动成功:");
        logger.error("======================启动成功:");
    }
}
