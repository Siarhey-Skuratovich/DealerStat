package com.leverx.dealerstat.dto.exception.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Violation {

  private String reason;
  private String message;
}
