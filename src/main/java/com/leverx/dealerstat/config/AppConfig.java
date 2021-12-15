package com.leverx.dealerstat.config;

import org.springframework.beans.factory.annotation.Autowired;
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
  public JavaMailSender getJavaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost("***REMOVED***");
    mailSender.setPort(***REMOVED***);

    mailSender.setUsername("***REMOVED***");
    mailSender.setPassword("***REMOVED***");

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.debug", "true");

    return mailSender;
  }
}
