package com.leverx.dealerstat.controller;

import com.leverx.dealerstat.model.ConfirmationUserCode;
import com.leverx.dealerstat.model.EmailLetter;
import com.leverx.dealerstat.model.UserEntity;
import com.leverx.dealerstat.model.dto.authorisation.passwordreset.CodeAndNewPasswordDto;
import com.leverx.dealerstat.model.dto.authorisation.passwordreset.EmailDto;
import com.leverx.dealerstat.service.email.EmailLetterService;
import com.leverx.dealerstat.service.confirmatiocode.ConfirmationCodeService;
import com.leverx.dealerstat.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
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
    return ResponseEntity.ok().build();
  }

  @PostMapping(value = "/logout")
  public ResponseEntity<?> logout() {
    return ResponseEntity.ok().build();
  }

  @PostMapping(value = "/auth/forgot_password")
  public ResponseEntity<?> sendCodeToEmailIfForgotPassword(@Valid @RequestBody EmailDto emailDto)
          throws MessagingException, UnsupportedEncodingException, InstantiationException {

    if (!userService.existsByEmail(emailDto.getEmail())) {
      return ResponseEntity.notFound().build();
    }

    UserEntity userEntity = userService.read(emailDto.getEmail());

    EmailLetter resetPasswordLetter = new EmailLetter(emailDto.getEmail());

    ConfirmationUserCode confirmationUserCode = confirmationCodeService.createConfirmationCodeFor(userEntity);

    resetPasswordLetter.compileResetPasswordLetter(userEntity.getFirstName(), confirmationUserCode);
    emailLetterService.sendLetter(resetPasswordLetter);

    return ResponseEntity.ok().build();
  }

  @PostMapping(value = "/auth/reset")
  public ResponseEntity<?> resetPassword(@Valid @RequestBody CodeAndNewPasswordDto codeAndNewPasswordDto) {

    Optional<ConfirmationUserCode> existedCode = confirmationCodeService.findByCode(codeAndNewPasswordDto.getCode());

    if (existedCode.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    Optional<UserEntity> userEntityOptional = userService.read(existedCode.get().getUserId());

    if (userEntityOptional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    UserEntity userEntity = userEntityOptional.get();
    userEntity.setPassword(passwordEncoder.encode(codeAndNewPasswordDto.getNewPassword()));
    userService.update(userEntity);

    confirmationCodeService.delete(userEntity.getUserId());

    return ResponseEntity.ok().build();
  }


    @GetMapping(value = "/auth/check_code")
    public ResponseEntity<?> checkIfResetCodeActual (@RequestBody ConfirmationUserCode codeToCheck){

      Optional<ConfirmationUserCode> existedCode = confirmationCodeService.findByCode(codeToCheck.getCode());

      if (existedCode.isPresent()) {
        return ResponseEntity.ok().build();
      }
      return ResponseEntity.notFound().build();
    }
  }
