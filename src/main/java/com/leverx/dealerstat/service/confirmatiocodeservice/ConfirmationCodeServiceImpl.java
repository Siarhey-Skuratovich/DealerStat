package com.leverx.dealerstat.service.confirmatiocodeservice;

import com.leverx.dealerstat.model.ConfirmationCodeOfUser;
import com.leverx.dealerstat.model.User;
import com.leverx.dealerstat.repositories.redis.ConfirmationCodeRepository;
import org.springframework.data.util.Streamable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConfirmationCodeServiceImpl implements ConfirmationCodeService {

  private final ConfirmationCodeRepository codeRepository;
  private final JavaMailSender mailSender;

  public ConfirmationCodeServiceImpl(ConfirmationCodeRepository codeRepository, JavaMailSender mailSender) {
    this.codeRepository = codeRepository;
    this.mailSender = mailSender;
  }

  @Override
  public void createFor(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
    ConfirmationCodeOfUser code = new ConfirmationCodeOfUser(user.hashCode());
    codeRepository.save(code);
    sendVerificationEmail(user, code, siteURL);
  }

  @Override
  public List<ConfirmationCodeOfUser> readAll() {
    return Streamable.of(codeRepository.findAll()).toList();
  }

  @Override
  public Optional<ConfirmationCodeOfUser> exists(int code) {
    return codeRepository.findById(code);
  }

  @Override
  public boolean update(ConfirmationCodeOfUser code, UUID id) {
    return false;
  }

  @Override
  public boolean delete(UUID id) {
    return false;
  }

  private void sendVerificationEmail(User user, ConfirmationCodeOfUser code, String appURL)
          throws MessagingException, UnsupportedEncodingException {
    String toAddress = user.getEmail();
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

    content = content.replace("[[name]]", user.getFirstName());
    String verifyURL = appURL + "/auth/confirm/" + code.getCode();

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
