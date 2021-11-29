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
  public ConfirmationUserCode createConfirmationCodeFor(UserEntity userEntity) throws InstantiationException {
    ConfirmationUserCode.Builder confirmationUserCodeBuilder = ConfirmationUserCode.newBuilder();

    confirmationUserCodeBuilder.setUserId(userEntity.getId());
    confirmationUserCodeBuilder.setCodeId(generateUniqueCode());

    ConfirmationUserCode confirmationUserCode = confirmationUserCodeBuilder.build();

    codeRepository.save(confirmationUserCode);
    return confirmationUserCode;
  }

  @Override
  public List<ConfirmationUserCode> readAll() {
    return Streamable.of(codeRepository.findAll()).toList();
  }

  @Override
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
    final int leftBound = 1000000000;
    int randomCode = secureRandom.nextInt(Integer.MAX_VALUE - leftBound) + leftBound;
    Optional<ConfirmationUserCode> existedCode = read(randomCode);
    while (existedCode.isPresent()) {
      randomCode = secureRandom.nextInt(Integer.MAX_VALUE - leftBound) + leftBound;
    }
    return randomCode;
  }
}
