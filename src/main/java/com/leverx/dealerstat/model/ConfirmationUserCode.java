package com.leverx.dealerstat.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@Getter
@NoArgsConstructor
@RedisHash(value = "Code", timeToLive = ConfirmationUserCode.TIME_TO_LIVE_IN_SECONDS)
public class ConfirmationUserCode {
  public static final int MIN_CODE_VALUE = 1000000000;
  public static final int MAX_CODE_VALUE = Integer.MAX_VALUE;
  public static final int TIME_TO_LIVE_IN_SECONDS = 86400;

  @Id
  private UUID userId;

  @Indexed
  private int code;

  public static Builder newBuilder() {
    return new ConfirmationUserCode().new Builder();
  }

  public class Builder {
    private Builder() {}

    public Builder setCode(int code) {
      ConfirmationUserCode.this.code = code;
      return this;
    }

    public Builder setUserId(UUID userId) {
      ConfirmationUserCode.this.userId = userId;
      return this;
    }

    public ConfirmationUserCode build() throws InstantiationException {
      if (code == 0 || userId == null) {
        throw new InstantiationException();
      }
      return ConfirmationUserCode.this;
    }
  }
}
