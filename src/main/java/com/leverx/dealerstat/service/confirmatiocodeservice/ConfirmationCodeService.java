package com.leverx.dealerstat.service.confirmatiocodeservice;

import com.leverx.dealerstat.model.ConfirmationUserCode;
import com.leverx.dealerstat.model.UserEntity;

import java.util.List;
import java.util.Optional;

public interface ConfirmationCodeService {

  ConfirmationUserCode createConfirmationCodeFor(UserEntity userEntity) throws InstantiationException;

  List<ConfirmationUserCode> readAll();

  Optional<ConfirmationUserCode> read(int codeId);

  boolean update(int codeId);

  boolean delete(int codeId);
}
