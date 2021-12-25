package com.leverx.dealerstat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.persistence.EntityManagerFactory;
import java.util.Properties;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.leverx.dealerstat")
@EnableTransactionManagement
public class AppConfig implements WebMvcConfigurer {

  private final EntityManagerFactory emf;

  public AppConfig(EntityManagerFactory emf) {
    this.emf = emf;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    OpenEntityManagerInViewInterceptor interceptor = new OpenEntityManagerInViewInterceptor();
    interceptor.setEntityManagerFactory(emf);
    registry.addWebRequestInterceptor(interceptor);
  }

  @Bean
  public JavaMailSender getJavaMailSender(@Value("${mailSender.host}") String host,
                                          @Value("${mailSender.port}") int port,
                                          @Value("${mailSender.username}") String username,
                                          @Value("${mailSender.password}") String password) {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(host);
    mailSender.setPort(port);

    mailSender.setUsername(username);
    mailSender.setPassword(password);

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.debug", "true");

    return mailSender;
  }
}
