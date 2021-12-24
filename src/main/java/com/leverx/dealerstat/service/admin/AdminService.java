package com.leverx.dealerstat.service.***REMOVED***;

import com.leverx.dealerstat.model.UserEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

@Service
public class AdminService {

  private final Properties securityProperties;

  public AdminService() throws IOException, URISyntaxException {
    FileReader reader = new FileReader(getClass().getClassLoader().getResource("security.properties").toURI().getPath());

    Properties securityProperties = new Properties();
    securityProperties.load(reader);
    this.securityProperties = securityProperties;
  }

  public String getAdminLogin() {
    return securityProperties.getProperty("***REMOVED***.login");
  }

  public String getEncryptedAdminPassword() {
    return new BCryptPasswordEncoder().encode(securityProperties.getProperty("***REMOVED***.password"));
  }

  public String getAdminRole() {
    return UserEntity.Role.ADMIN.name();
  }
}
