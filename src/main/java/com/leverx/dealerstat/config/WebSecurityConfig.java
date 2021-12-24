package com.leverx.dealerstat.config;

import com.leverx.dealerstat.service.***REMOVED***.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@ComponentScan(value = "com.leverx.dealerstat.service.***REMOVED***")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  private final DataSource dataSource;
  private final AdminService ***REMOVED***Service;

  public WebSecurityConfig(DataSource dataSource, AdminService ***REMOVED***Service) {
    this.dataSource = dataSource;
    this.***REMOVED***Service = ***REMOVED***Service;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
            .authorizeRequests()
            .antMatchers("/registration", "/auth/**")
            .permitAll()
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/articles")
            .permitAll()
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/***REMOVED***istration/**")
            .hasRole(***REMOVED***Service.getAdminRole())
            .and()
            .authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .formLogin()
            .loginProcessingUrl("/auth")
            .disable()
            .httpBasic()
            .authenticationEntryPoint(authenticationEntryPoint())
            .and()
            .csrf()
            .disable();
  }

  @Autowired
  protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth
            .jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery("select email,password,enabled "
                    + "from users "
                    + "where email = ?")
            .authoritiesByUsernameQuery("select email,role "
                    + "from users "
                    + "where email = ?")
            .passwordEncoder(new BCryptPasswordEncoder());
  }

  @Autowired
  public void initialize(AuthenticationManagerBuilder builder) throws Exception {
    builder.inMemoryAuthentication()
            .withUser(***REMOVED***Service.getAdminLogin())
            .password(***REMOVED***Service.getEncryptedAdminPassword())
            .roles(***REMOVED***Service.getAdminRole());
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint(){
    return new AuthenticationEntryPointImpl();
  }
}
