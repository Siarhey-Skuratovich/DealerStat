package com.leverx.dealerstat.service.confirmatiocodeservice;

import com.leverx.dealerstat.model.ConfirmationUserCode;
import com.leverx.dealerstat.model.UserEntity;
import com.leverx.dealerstat.repository.redis.ConfirmationCodeRepository;
import org.springframework.data.util.Streamable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@Service
public class ConfirmationCodeServiceImpl implements ConfirmationCodeService {

  private final ConfirmationCodeRepository codeRepository;
  private final JavaMailSender mailSender;

  public ConfirmationCodeServiceImpl(ConfirmationCodeRepository codeRepository, JavaMailSender mailSender) {
    this.codeRepository = codeRepository;
    this.mailSender = mailSender;
  }

  @Override
  public void createFor(UserEntity userEntity, String siteURL) throws MessagingException, UnsupportedEncodingException {
    ConfirmationUserCode code = new ConfirmationUserCode(userEntity.hashCode(), userEntity.getId());
    codeRepository.save(code);
    sendVerificationEmail(userEntity, code, siteURL);
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

  private void sendVerificationEmail(UserEntity userEntity, ConfirmationUserCode code, String appURL)
          throws MessagingException, UnsupportedEncodingException {
    String toAddress = userEntity.getEmail();
    String fromAddress = "***REMOVED***";
    String senderName = "Dealer Stat";
    String subject = "Please confirm your registration";
    String content = "Dear [[name]],<br>"
            + "Please click the link below to confirm your registration:<br>"
            + "<h3><a href=\"[[URL]]\" target=\"_self\">CONFIRM</a></h3>"
            + "Thank you,<br>"
            + "Dealer Stat.";

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);

    helper.setFrom(fromAddress, senderName);
    helper.setTo(toAddress);
    helper.setSubject(subject);

    content = content.replace("[[name]]", userEntity.getFirstName());
    String verifyURL = appURL + "/auth/confirm/" + code.getCodeId();

    content = content.replace("[[URL]]", verifyURL);

    helper.setText(content, true);

    mailSender.send(message);
  }

//  private String generateCode() {
//    StringBuilder codeBuilder = new StringBuilder();
//    SecureRandom secureRandom = new SecureRandom();
//    for (int i = 0; i < 10; i++) {
//      codeBuilder.append(secureRandom.nextInt(10));
//    }
//    return codeBuilder.toString();
//  }
}
