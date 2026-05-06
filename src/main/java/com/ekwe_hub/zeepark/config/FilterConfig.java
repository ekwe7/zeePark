package com.ekwe_hub.zeepark.config;

import com.ekwe_hub.zeepark.filter.SessionFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<SessionFilter> sessionFilterRegistration(SessionFilter filter) {
        FilterRegistrationBean<SessionFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.addUrlPatterns("/api/*"); //protect all Api
        registration.setOrder(1);
        return registration;
    }
}