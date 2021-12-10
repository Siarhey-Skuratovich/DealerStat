package com.leverx.dealerstat.service.confirmatiocode;

import com.leverx.dealerstat.model.ConfirmationUserCode;
import com.leverx.dealerstat.model.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConfirmationCodeService {

  ConfirmationUserCode createConfirmationCodeFor(UserEntity userEntity) throws InstantiationException;

  List<ConfirmationUserCode> readAll();

  Optional<ConfirmationUserCode> read(UUID userId);

  boolean delete(UUID userId);

  Optional<ConfirmationUserCode> findByCode(int code);
}
