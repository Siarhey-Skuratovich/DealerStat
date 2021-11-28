package com.leverx.dealerstat.config;

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
  private static final String ADMIN_PASSWORD = new BCryptPasswordEncoder().encode("***REMOVED***");
  private static final String ADMIN_ROLE = UserEntity.Role.Admin.name();


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
    http
            .authorizeRequests()
            .antMatchers("/registration", "/auth/confirm/{codeId}")
            .permitAll()
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/users", "/codes")
            .hasRole(ADMIN_ROLE)
            .and()
            .authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .formLogin()
            .loginProcessingUrl("/auth")
            .disable()
            .httpBasic()
            .authenticationEntryPoint(authenticationEntryPoint)
            .and()
            .csrf()
            .ignoringAntMatchers("/registration", "/auth", "/logout");
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
            .password(ADMIN_PASSWORD).roles(ADMIN_ROLE);
  }
}
