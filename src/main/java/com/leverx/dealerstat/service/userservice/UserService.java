package com.leverx.dealerstat.service.userservice;

import com.leverx.dealerstat.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

  void create(User user);

  List<User> readAll();

  User read(UUID id);

  boolean update(User user);

  boolean delete(UUID id);
}
