package com.tutego.date4u.core.config;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;



@Configuration
public class ThymeleafTime {
   
    //TemplateEngine templateEngine = new TemplateEngine();
        
        @Bean
        public Java8TimeDialect java8TimeDialect() {
            return new Java8TimeDialect();
        }
        
        @Bean
        public LayoutDialect layoutDialect() {
            return new LayoutDialect();
        }
}
