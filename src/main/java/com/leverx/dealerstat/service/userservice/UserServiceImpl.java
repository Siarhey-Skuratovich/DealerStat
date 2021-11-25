package com.leverx.dealerstat.service.userservice;

import com.leverx.dealerstat.model.ConfirmationUserCode;
import com.leverx.dealerstat.model.User;
import com.leverx.dealerstat.repository.postgresql.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void create(User user) {
    user.setId(UUID.randomUUID());
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setLocalDateTime(LocalDateTime.now());
    user.setRole(User.Role.Trader);
    user.setEnabled(false);
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

  @Override
  public boolean confirmUserBy(ConfirmationUserCode existedCode) {
      User user = read(existedCode.getUserId());
      user.setEnabled(true);
      return update(user);
  }
}
