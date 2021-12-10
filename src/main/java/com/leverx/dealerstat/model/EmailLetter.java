package com.leverx.dealerstat.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Getter
@NoArgsConstructor
public class EmailLetter {

  private final String fromAddress = "***REMOVED***";
  private final String senderName = "Dealer Stat";

  private String destinationEmail;
  private String subject;
  private String content;

  public EmailLetter(String destinationEmail) {
    this.destinationEmail = destinationEmail;
  }

  public void compileRegistrationLetter(String firstName, ConfirmationUserCode code, String appURL) {
    subject = "Please confirm your registration";
    content = "Dear [[name]],<br>"
            + "Please click the link below to confirm your registration:<br>"
            + "<h3><a href=\"[[URL]]\" target=\"_self\">CONFIRM</a></h3>"
            + "Thank you,<br>"
            + "Dealer Stat.";

    content = content.replace("[[name]]", firstName);
    String verifyURL = appURL + "/auth/confirm/" + code.getCode();
    content = content.replace("[[URL]]", verifyURL);
  }

  public void compileResetPasswordLetter(String firstName, ConfirmationUserCode code) {
    subject = "Code to reset password";
    content = "Dear [[name]],<br>"
            + "Here is the code to confirm your intention to reset password:<br>"
            + "<h3>[[code]]</h3>"
            + "Dealer Stat.";
    content = content.replace("[[name]]", firstName);
    content = content.replace("[[code]]", String.valueOf(code.getCode()));
  }
}
