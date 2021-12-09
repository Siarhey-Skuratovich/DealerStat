package com.leverx.dealerstat.controller;

import com.leverx.dealerstat.model.ConfirmationUserCode;
import com.leverx.dealerstat.model.EmailLetter;
import com.leverx.dealerstat.model.UserEntity;
import com.leverx.dealerstat.service.email.EmailLetterService;
import com.leverx.dealerstat.service.confirmatiocode.ConfirmationCodeService;
import com.leverx.dealerstat.service.user.UserService;
import com.leverx.dealerstat.validation.groups.InfoUserShouldPass;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@Validated
public class RegistrationController {
  private final ConfirmationCodeService confirmationCodeService;
  private final UserService userService;
  private final EmailLetterService emailLetterService;

  public RegistrationController(ConfirmationCodeService confirmationCodeService, UserService userService, EmailLetterService emailLetterService) {
    this.confirmationCodeService = confirmationCodeService;
    this.userService = userService;
    this.emailLetterService = emailLetterService;
  }

  @SneakyThrows
  @PostMapping(value = "/registration")
  @Transactional
  public ResponseEntity<?> createNewUser(@Validated(InfoUserShouldPass.class) @RequestBody UserEntity userEntity,
                                         HttpServletRequest request) {
    if (userService.containsNoSuchEmail(userEntity.getEmail())) {

      userService.create(userEntity);

      ConfirmationUserCode ConfirmationUserCode = confirmationCodeService.createConfirmationCodeFor(userEntity);

      EmailLetter registrationConfirmationLetter = new EmailLetter(userEntity.getEmail());
      registrationConfirmationLetter.compileRegistrationLetter(userEntity.getFirstName(), ConfirmationUserCode, getAppURL(request));

      emailLetterService.sendLetter(registrationConfirmationLetter);

      return new ResponseEntity<>(HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
  }

  @GetMapping(value = "/auth/confirm/{codeId}")
  public ResponseEntity<?> checkConfirmationCode(@PathVariable int codeId) {
    Optional<ConfirmationUserCode> existedCode = confirmationCodeService.read(codeId);
    if (existedCode.isPresent()) {

      userService.confirmUserBy(existedCode.get());
      confirmationCodeService.delete(existedCode.get().getCodeId());

      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  private String getAppURL(HttpServletRequest request) {
    String siteURL = request.getRequestURL().toString();
    return siteURL.replace(request.getServletPath(), "");
  }
}
