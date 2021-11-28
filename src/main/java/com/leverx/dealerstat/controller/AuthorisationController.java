package com.leverx.dealerstat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorisationController {

  @PostMapping(value = "/auth")
  public ResponseEntity<?> auth() {
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping(value = "/logout")
  public ResponseEntity<?> logout() {
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
