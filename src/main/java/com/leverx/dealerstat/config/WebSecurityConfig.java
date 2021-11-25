package com.leverx.dealerstat.config;

import com.leverx.dealerstat.AuthenticationEntryPointImpl;
import com.leverx.dealerstat.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  private final DataSource dataSource;
  private final AuthenticationEntryPointImpl authenticationEntryPoint;

  private static final String ADMIN_USERNAME = "***REMOVED***";
  private static final String ADMIN_PASSWORD;

  static  {
    String password = "***REMOVED***";
    ADMIN_PASSWORD = new BCryptPasswordEncoder().encode(password);
  }


  public WebSecurityConfig(DataSource dataSource, AuthenticationEntryPointImpl authenticationEntryPoint) {
    this.dataSource = dataSource;
    this.authenticationEntryPoint = authenticationEntryPoint;

  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http        //csrf().disable();
            .authorizeRequests()
            .antMatchers("/registration", "/auth/confirm/{codeId}")
            .permitAll()
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/users", "/codes")
            .hasRole("Admin")
            .and()
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginProcessingUrl("/auth")
            .permitAll()
            .and()
            .httpBasic()
            .authenticationEntryPoint(authenticationEntryPoint)
            .and()
            .csrf()
            .ignoringAntMatchers("/registration");
//            .anyRequest().permitAll();
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
    builder.inMemoryAuthentication().withUser(ADMIN_USERNAME)
            .password(ADMIN_PASSWORD).roles(UserEntity.Role.Admin.name());
  }
}
