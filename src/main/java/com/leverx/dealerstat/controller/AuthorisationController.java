package com.leverx.dealerstat.controller;

import com.leverx.dealerstat.model.ConfirmationUserCode;
import com.leverx.dealerstat.model.EmailLetter;
import com.leverx.dealerstat.model.UserEntity;
import com.leverx.dealerstat.model.dto.authorisation.CodeAndNewPassword;
import com.leverx.dealerstat.service.EmailLetterService;
import com.leverx.dealerstat.service.confirmatiocode.ConfirmationCodeService;
import com.leverx.dealerstat.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@RestController
public class AuthorisationController {
  private final UserService userService;
  private final ConfirmationCodeService confirmationCodeService;
  private final EmailLetterService emailLetterService;
  private final PasswordEncoder passwordEncoder;

  public AuthorisationController(UserService userService, ConfirmationCodeService confirmationCodeService, EmailLetterService emailLetterService, PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.confirmationCodeService = confirmationCodeService;
    this.emailLetterService = emailLetterService;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping(value = "/auth")
  public ResponseEntity<?> auth() {
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping(value = "/logout")
  public ResponseEntity<?> logout() {
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping(value = "/auth/forgot_password")
  public ResponseEntity<?> sendCodeToEmailIfForgotPassword(@RequestBody EmailLetter resetPasswordLetter)
          throws MessagingException, UnsupportedEncodingException, InstantiationException {

    UserEntity userEntity = userService.read(resetPasswordLetter.getDestinationEmail());
    if (userEntity == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    deleteIfAlreadyContainsCodeFor(userEntity);

    ConfirmationUserCode confirmationUserCode = confirmationCodeService.createConfirmationCodeFor(userEntity);

    resetPasswordLetter.makeResetPasswordLetter(userEntity.getFirstName(), confirmationUserCode);
    emailLetterService.sendLetter(resetPasswordLetter);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping(value = "/auth/reset")
  public ResponseEntity<?> resetPassword(@RequestBody CodeAndNewPassword codeAndNewPassword) {

    Optional<ConfirmationUserCode> existedCode = confirmationCodeService.read(codeAndNewPassword.getCode());

    if (existedCode.isPresent()) {
      UserEntity userEntity = userService.read(existedCode.get().getUserId());
      userEntity.setPassword(passwordEncoder.encode(codeAndNewPassword.getNewPassword()));
      userService.update(userEntity);

      confirmationCodeService.delete(codeAndNewPassword.getCode());

      return new ResponseEntity<>(HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @GetMapping(value = "/auth/check_code")
  public ResponseEntity<?> checkIfResetCodeActual(@RequestBody ConfirmationUserCode codeToCheck) {
    Optional<ConfirmationUserCode> existedCode = confirmationCodeService.read(codeToCheck.getCodeId());
    if (existedCode.isPresent()) {
      return new ResponseEntity<>(HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  private void deleteIfAlreadyContainsCodeFor(UserEntity userEntity) {
    List<ConfirmationUserCode> userCodeList = confirmationCodeService.readAll();
    for (ConfirmationUserCode confirmationUserCode : userCodeList) {
      if (confirmationUserCode.getUserId().equals(userEntity.getUserId())) {
        confirmationCodeService.delete(confirmationUserCode.getCodeId());
      }
    }
  }
}
