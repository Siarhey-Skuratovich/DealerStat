package com.leverx.dealerstat.service.userservice;

import com.leverx.dealerstat.model.ConfirmationUserCode;
import com.leverx.dealerstat.model.UserEntity;
import com.leverx.dealerstat.repository.postgresql.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
  public void create(UserEntity userEntity) {
    userEntity.setId(UUID.randomUUID());
    userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
    userEntity.setLocalDateTime(LocalDateTime.now());
    userEntity.setRole(UserEntity.Role.Trader);
    userEntity.setEnabled(false);
    userRepository.save(userEntity);
  }

  @Override
  public List<UserEntity> readAll() {
    return userRepository.findAll();
  }

  @Override
  public UserEntity read(UUID id) {
    return userRepository.getById(id);
  }

  @Override
  public boolean update(UserEntity userEntity) {
    if (userRepository.existsById(userEntity.getId())) {
      userRepository.save(userEntity);
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
      UserEntity userEntity = read(existedCode.getUserId());
      userEntity.setEnabled(true);
      return update(userEntity);
  }
}
