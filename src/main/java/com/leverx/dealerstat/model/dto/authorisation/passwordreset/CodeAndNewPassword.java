package com.leverx.dealerstat.model.dto.authorisation.passwordreset;

import lombok.Getter;

@Getter
public class CodeAndNewPassword {
  private int code;
  private String newPassword;
}
