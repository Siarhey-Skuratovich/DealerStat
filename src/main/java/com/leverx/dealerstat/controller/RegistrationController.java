package com.leverx.dealerstat.controller;

import com.leverx.dealerstat.model.ConfirmationUserCode;
import com.leverx.dealerstat.model.User;
import com.leverx.dealerstat.service.confirmatiocodeservice.ConfirmationCodeService;
import com.leverx.dealerstat.service.userservice.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class RegistrationController {
  private final ConfirmationCodeService confirmationCodeService;
  private final UserService userService;

  public RegistrationController(ConfirmationCodeService confirmationCodeService, UserService userService) {
    this.confirmationCodeService = confirmationCodeService;
    this.userService = userService;
  }


  @PostMapping (value = "/registration")
  public ResponseEntity<?> createNewUser(@RequestBody User user, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
    user.setId(UUID.randomUUID());
    LocalDateTime localDateTime = LocalDateTime.now();
    user.setLocalDateTime(localDateTime);
    user.setRole(User.Role.Trader);
    user.setEnabled(false);
    userService.create(user);
    confirmationCodeService.createFor(user, getAppURL(request));
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping(value = "/auth/confirm/{code}")
  public ResponseEntity<?> checkConfirmationCode(@PathVariable int code) {
    Optional<ConfirmationUserCode> existedCode = confirmationCodeService.read(code);
    if (existedCode.isPresent()) {
      User user = userService.read(existedCode.get().getUserId());
      user.setEnabled(true);
      userService.update(user);
      confirmationCodeService.delete(existedCode.get().getCodeId());
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping(value = "/codes")
  public ResponseEntity<List<ConfirmationUserCode>> readCodes() {
    final List<ConfirmationUserCode> codes = confirmationCodeService.readAll();
    return codes != null && !codes.isEmpty()
            ? new ResponseEntity<>(codes, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @GetMapping(value = "/users")
  public ResponseEntity<List<User>> readUsers() {
    final List<User> users = userService.readAll();
    return users != null && !users.isEmpty()
            ? new ResponseEntity<>(users, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  private String getAppURL(HttpServletRequest request) {
    String siteURL = request.getRequestURL().toString();
    return siteURL.replace(request.getServletPath(), "");
  }
}
