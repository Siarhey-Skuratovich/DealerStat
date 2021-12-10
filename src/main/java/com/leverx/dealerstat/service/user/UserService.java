package com.leverx.dealerstat.service.user;

import com.leverx.dealerstat.model.ConfirmationUserCode;
import com.leverx.dealerstat.model.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  void create(UserEntity userEntity);

  List<UserEntity> readAll();

  Optional<UserEntity> read(UUID id);

  UserEntity read(String email);

  boolean update(UserEntity userEntity);

  boolean delete(UUID id);

  boolean confirmUserBy(ConfirmationUserCode existedCode);

  boolean existsByEmail(String email);

  boolean noUserById(UUID id);
}
