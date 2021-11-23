package com.leverx.dealerstat.service.userservice;

import com.leverx.dealerstat.model.User;
import com.leverx.dealerstat.repositories.postgresql.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void create(User user) {
    userRepository.save(user);
  }

  @Override
  public List<User> readAll() {
    return userRepository.findAll();
  }

  @Override
  public User read(UUID id) {
    return userRepository.getById(id);
  }

  @Override
  public boolean update(User user) {
    if (userRepository.existsById(user.getId())) {
      userRepository.save(user);
      return true;
    }
    return false;
  }

  @Override
  public boolean delete(UUID id) {
    if (userRepository.existsById(id)) {
      userRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
