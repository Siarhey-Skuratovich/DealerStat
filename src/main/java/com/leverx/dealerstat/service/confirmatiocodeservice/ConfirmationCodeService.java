package com.leverx.dealerstat.service.confirmatiocodeservice;

import com.leverx.dealerstat.model.ConfirmationCodeOfUser;
import com.leverx.dealerstat.model.User;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConfirmationCodeService {

  void createFor(User user, String siteURL) throws MessagingException, UnsupportedEncodingException;

  List<ConfirmationCodeOfUser> readAll();

  Optional<ConfirmationCodeOfUser> read(int code);

  boolean update(ConfirmationCodeOfUser code);

  boolean delete(UUID id);
}
