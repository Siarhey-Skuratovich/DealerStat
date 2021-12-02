package com.leverx.dealerstat.service.serviceof;

import java.util.List;
import java.util.UUID;

public interface ServiceOf<T>{
  void create(T t);

  List<T> readAll();

  T read(UUID id);

  boolean update(T t);

  boolean delete(UUID id);
}
