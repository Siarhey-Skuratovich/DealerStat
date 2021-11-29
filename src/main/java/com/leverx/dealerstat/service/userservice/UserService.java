package com.leverx.dealerstat.service.userservice;

import com.leverx.dealerstat.model.ConfirmationUserCode;
import com.leverx.dealerstat.model.UserEntity;

import java.util.List;
import java.util.UUID;

public interface UserService {

  void create(UserEntity userEntity);

  List<UserEntity> readAll();

  UserEntity read(UUID id);

  UserEntity read(String email);

  boolean update(UserEntity userEntity);

  boolean delete(UUID id);

  boolean confirmUserBy(ConfirmationUserCode existedCode);

  boolean containsNoSuch(String email);
}
