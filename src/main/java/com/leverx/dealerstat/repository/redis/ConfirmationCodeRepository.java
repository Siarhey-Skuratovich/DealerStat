package com.leverx.dealerstat.repository.redis;

import com.leverx.dealerstat.model.ConfirmationUserCode;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationCodeRepository extends KeyValueRepository<ConfirmationUserCode, Integer> {}
