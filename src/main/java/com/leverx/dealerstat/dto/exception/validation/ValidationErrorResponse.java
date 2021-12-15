package com.leverx.dealerstat.dto.exception.validation;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationErrorResponse {
  private final List<Violation> violations = new ArrayList<>();
}

