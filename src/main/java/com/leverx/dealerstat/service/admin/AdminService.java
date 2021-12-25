package com.leverx.dealerstat.service.***REMOVED***;

import com.leverx.dealerstat.model.UserEntity;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

  private final Environment environment;

  public AdminService(Environment environment)  {
    this.environment = environment;
  }

  public String getAdminLogin() {
    return environment.getProperty("***REMOVED***.login");
  }

  public String getEncryptedAdminPassword() {
    return new BCryptPasswordEncoder().encode(environment.getProperty("***REMOVED***.password"));
  }

  public String getAdminRole() {
    return UserEntity.Role.ADMIN.name();
  }
}
