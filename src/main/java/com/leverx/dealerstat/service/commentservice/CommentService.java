package com.leverx.dealerstat.service.commentservice;

import com.leverx.dealerstat.model.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentService {

  void create(Comment comment);

  List<Comment> readAll();

  Comment read(UUID id);

  boolean update(Comment comment);

  boolean delete(UUID id);
}
