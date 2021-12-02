package com.leverx.dealerstat.controller;

import com.leverx.dealerstat.model.ConfirmationUserCode;
import com.leverx.dealerstat.model.UserEntity;
import com.leverx.dealerstat.service.confirmatiocode.ConfirmationCodeService;
import com.leverx.dealerstat.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminController {
  private final ConfirmationCodeService confirmationCodeService;
  private final UserService userService;

  public AdminController(ConfirmationCodeService confirmationCodeService, UserService userService) {
    this.confirmationCodeService = confirmationCodeService;
    this.userService = userService;
  }

  @GetMapping(value = "/codes")
  public ResponseEntity<List<ConfirmationUserCode>> readCodes() {
    final List<ConfirmationUserCode> codes = confirmationCodeService.readAll();
    return codes != null && !codes.isEmpty()
            ? new ResponseEntity<>(codes, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @GetMapping(value = "/users")
  public ResponseEntity<List<UserEntity>> readUsers() {
    final List<UserEntity> userEntities = userService.readAll();
    return userEntities != null && !userEntities.isEmpty()
            ? new ResponseEntity<>(userEntities, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }
}
