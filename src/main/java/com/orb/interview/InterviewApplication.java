package com.orb.interview;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Main class for interview application
 */
@SpringBootApplication
@EnableAsync
public class InterviewApplication {
  public static void main(String[] args) {
    SpringApplication.run(InterviewApplication.class, args);
  }

  @Bean
  public Executor taskExecutor() {
    int cpu_num = Runtime.getRuntime().availableProcessors();

    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(Math.min(2, cpu_num));
    executor.setMaxPoolSize(cpu_num);
    executor.setQueueCapacity(500);
    executor.setThreadNamePrefix("worker-pool-");
    executor.initialize();
    return executor;
  }

  @Configuration
  public static class ApplicationConfiguration implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
    @Value("${interview.server.port}")
    private int interviewServerPort;

    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
      factory.setPort(interviewServerPort);
    }
  }
}
