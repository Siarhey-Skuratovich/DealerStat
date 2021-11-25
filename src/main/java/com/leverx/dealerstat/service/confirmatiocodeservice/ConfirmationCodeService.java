package com.leverx.dealerstat.service.confirmatiocodeservice;

import com.leverx.dealerstat.model.ConfirmationUserCode;
import com.leverx.dealerstat.model.UserEntity;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

public interface ConfirmationCodeService {

  void createFor(UserEntity userEntity, String siteURL) throws MessagingException, UnsupportedEncodingException;

  List<ConfirmationUserCode> readAll();

  Optional<ConfirmationUserCode> read(int codeId);

  boolean update(int codeId);

  boolean delete(int codeId);
}
