package com.leverx.dealerstat.service.confirmatiocodeservice;

import com.leverx.dealerstat.model.ConfirmationUserCode;
import com.leverx.dealerstat.model.UserEntity;
import com.leverx.dealerstat.repository.redis.ConfirmationCodeRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class ConfirmationCodeServiceImpl implements ConfirmationCodeService {

  private final ConfirmationCodeRepository codeRepository;

  public ConfirmationCodeServiceImpl(ConfirmationCodeRepository codeRepository) {
    this.codeRepository = codeRepository;
  }

  @Override
  public Optional<ConfirmationUserCode> createConfirmationCodeFor(UserEntity userEntity) {
    ConfirmationUserCode.Builder confirmationUserCodeBuilder = ConfirmationUserCode.newBuilder();

    confirmationUserCodeBuilder.setUserId(userEntity.getId());
    confirmationUserCodeBuilder.setCodeId(generateUniqueCode());

    ConfirmationUserCode confirmationUserCode;
    try {
      confirmationUserCode = confirmationUserCodeBuilder.build();
    } catch (InstantiationException e) {
      e.printStackTrace();
      return Optional.empty();
    }

    codeRepository.save(confirmationUserCode);
    return Optional.of(confirmationUserCode);
  }

  @Override
  public List<ConfirmationUserCode> readAll() {
    return Streamable.of(codeRepository.findAll()).toList();
  }

  public Optional<ConfirmationUserCode> read(int codeId) {
    return codeRepository.findById(codeId);
  }

  @Override
  public boolean update(int codeId) {
    return false;
  }

  @Override
  public boolean delete(int codeId) {
    if (codeRepository.existsById(codeId)) {
      codeRepository.deleteById(codeId);
      return true;
    }
    return false;
  }

  private int generateUniqueCode() {
    SecureRandom secureRandom = new SecureRandom();
    int randomCode = secureRandom.nextInt() * 1000000000;
    Optional<ConfirmationUserCode> existedCode = read(randomCode);
    while (existedCode.isPresent()) {
      randomCode = secureRandom.nextInt() * 1000000000;
    }
    return randomCode;
  }
}
