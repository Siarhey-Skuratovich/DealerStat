package com.leverx.dealerstat.dto.updation.comment;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UpdatedCommentMessageDto {

  @NotBlank
  private String message;
}
