package com.leverx.dealerstat.model.dto.authorisation.passwordreset;

import com.leverx.dealerstat.model.ConfirmationUserCode;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class CodeAndNewPasswordDto {

  @NotNull
  @Min(ConfirmationUserCode.MIN_CODE_VALUE)
  @Max(Integer.MAX_VALUE)
  private int code;

  @NotBlank
  private String newPassword;
}
