package com.leverx.dealerstat.service.confirmatiocode;

import com.leverx.dealerstat.model.ConfirmationUserCode;
import com.leverx.dealerstat.model.UserEntity;
import com.leverx.dealerstat.repository.redis.ConfirmationCodeRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConfirmationCodeServiceImpl implements ConfirmationCodeService {
  private final ConfirmationCodeRepository codeRepository;

  public ConfirmationCodeServiceImpl(ConfirmationCodeRepository codeRepository) {
    this.codeRepository = codeRepository;
  }

  @Override
  public ConfirmationUserCode createConfirmationCodeFor(UserEntity userEntity) {
    ConfirmationUserCode confirmationUserCode = ConfirmationUserCode.builder()
            .userId(userEntity.getUserId())
            .code(generateUniqueCode())
            .build();
    codeRepository.save(confirmationUserCode);
    return confirmationUserCode;
  }

  @Override
  public List<ConfirmationUserCode> readAll() {
    return Streamable.of(codeRepository.findAll()).toList();
  }

  @Override
  public Optional<ConfirmationUserCode> read(UUID userId) {
    return codeRepository.findById(userId);
  }


  @Override
  public boolean delete(UUID userId) {
    if (codeRepository.existsById(userId)) {
      codeRepository.deleteById(userId);
      return true;
    }
    return false;
  }

  @Override
  public Optional<ConfirmationUserCode> findByCode(int code) {
    return codeRepository.findByCode(code);
  }

  private int generateUniqueCode() {
    SecureRandom secureRandom = new SecureRandom();
    int randomCode = secureRandom.nextInt(
            ConfirmationUserCode.MAX_CODE_VALUE - ConfirmationUserCode.MIN_CODE_VALUE) + ConfirmationUserCode.MIN_CODE_VALUE;
    Optional<ConfirmationUserCode> existedCode = findByCode(randomCode);
    while (existedCode.isPresent()) {
      randomCode = secureRandom.nextInt(
              ConfirmationUserCode.MAX_CODE_VALUE - ConfirmationUserCode.MIN_CODE_VALUE) + ConfirmationUserCode.MIN_CODE_VALUE;
    }
    return randomCode;
  }
}
