package com.example.demo.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PortalWebConfig implements WebMvcConfigurer {
    private final PortalAccessInterceptor access;
    public PortalWebConfig(PortalAccessInterceptor access) { this.access = access; }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(access).addPathPatterns("/admin/**", "/admins/**", "/operators/**", "/clients/**",
                "/login", "/staff/login", "/logout");
    }
}
