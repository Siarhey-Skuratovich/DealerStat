package com.leverx.dealerstat.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@Getter
@RedisHash(value = "Code", timeToLive = 86400)
public class ConfirmationUserCode {
  @Id
  private int codeId;
  private UUID userId;

  private ConfirmationUserCode() {}

  public static Builder newBuilder() {
    return new ConfirmationUserCode().new Builder();
  }

  public class Builder {
    private Builder() {}

    public Builder setCodeId(int codeId) {
      ConfirmationUserCode.this.codeId = codeId;
      return this;
    }

    public Builder setUserId(UUID userId) {
      ConfirmationUserCode.this.userId = userId;
      return this;
    }

    public ConfirmationUserCode build() throws InstantiationException {
      if (codeId == 0 || userId == null) {
        throw new InstantiationException();
      }
      return ConfirmationUserCode.this;
    }
  }
}
