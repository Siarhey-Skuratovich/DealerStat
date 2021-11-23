package com.leverx.dealerstat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@AllArgsConstructor
@Data
@RedisHash(value = "Code", timeToLive = 86400)
public class ConfirmationCodeOfUser implements Serializable {
  @Id
  private int code;
}
