package ru.softwave.pool.config;

import freemarker.template.TemplateExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.ui.freemarker.SpringTemplateLoader;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppConfig implements WebMvcConfigurer {
  @Bean
  public ExecutorService executorService() {
    return Executors.newCachedThreadPool();
  }

  @Bean
  public freemarker.template.Configuration configuration() {
    freemarker.template.Configuration configuration =
        new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_30);
    configuration.setDefaultEncoding("UTF-8");
    configuration.setTemplateLoader(
        new SpringTemplateLoader(new ClassRelativeResourceLoader(this.getClass()), "/"));
    configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    return configuration;
  }
  @Bean
  public DeviceResolverHandlerInterceptor
  deviceResolverHandlerInterceptor() {
    return new DeviceResolverHandlerInterceptor();
  }

  @Bean
  public DeviceHandlerMethodArgumentResolver
  deviceHandlerMethodArgumentResolver() {
    return new DeviceHandlerMethodArgumentResolver();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(deviceResolverHandlerInterceptor());
  }

  @Override
  public void addArgumentResolvers(
          List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(deviceHandlerMethodArgumentResolver());
  }
}
