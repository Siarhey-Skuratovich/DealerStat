package com.leverx.dealerstat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@Data
@RedisHash(value = "Code", timeToLive = 86400)
public class ConfirmationUserCode implements Serializable {
  @Id
  private int codeId;
  private UUID userId;
}
