package com.leverx.dealerstat.controller;

import com.leverx.dealerstat.service.userservice.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorisationController {
  private final UserService userService;

  public AuthorisationController(UserService userService) {
    this.userService = userService;
  }

//  @PostMapping
//  public ResponseEntity<?> authorise() {
//
//  }
}
