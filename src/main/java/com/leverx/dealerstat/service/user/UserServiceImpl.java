package com.leverx.dealerstat.service.user;

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
    userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
    userEntity.setCreatedAt(LocalDateTime.now());
    userEntity.setEnabled(false);

    if (userEntity.getRole().equals(UserEntity.Role.ADMIN)) {
      userEntity.setRole(null);
    }

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
  public UserEntity read(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public boolean update(UserEntity userEntity) {
    if (userRepository.existsById(userEntity.getUserId())) {
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

  public boolean existsByEmail(String email) {
    return userRepository.findByEmail(email) != null;
  }

  @Override
  public boolean noUserById(UUID id) {
    return !userRepository.existsById(id);
  }
}
