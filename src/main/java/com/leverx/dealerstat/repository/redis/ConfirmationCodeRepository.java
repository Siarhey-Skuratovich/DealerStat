package com.leverx.dealerstat.repository.redis;

import com.leverx.dealerstat.model.ConfirmationUserCode;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfirmationCodeRepository extends KeyValueRepository<ConfirmationUserCode, UUID> {
  Optional<ConfirmationUserCode> findByCode(int code);
}
