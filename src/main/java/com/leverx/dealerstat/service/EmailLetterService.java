package com.leverx.dealerstat.service;

import com.leverx.dealerstat.model.EmailLetter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class EmailLetterService {
  private final JavaMailSender mailSender;

  public EmailLetterService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void sendLetter(EmailLetter letter) throws MessagingException, UnsupportedEncodingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);
    helper.setFrom(letter.getFromAddress(), letter.getSenderName());
    helper.setTo(letter.getDestinationEmail());
    helper.setSubject(letter.getSubject());
    helper.setText(letter.getContent(), true);
    mailSender.send(message);
  }
}
