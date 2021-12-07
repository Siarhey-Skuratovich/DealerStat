package com.leverx.dealerstat.service.serviceof.impl;

import com.leverx.dealerstat.model.Comment;
import com.leverx.dealerstat.repository.postgresql.CommentRepository;
import com.leverx.dealerstat.service.serviceof.ServiceOf;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService implements ServiceOf<Comment> {
  private final CommentRepository commentRepository;

  public CommentService(CommentRepository commentRepository) {
    this.commentRepository = commentRepository;
  }

  @Override
  public Comment create(Comment comment) {
    comment.setCreatedAt(LocalDateTime.now());
    comment.setApproved(false);
    return commentRepository.save(comment);
  }

  @Override
  public List<Comment> readAll() {
    return commentRepository.findAll();
  }

  @Override
  public Comment read(UUID id) {
    return commentRepository.getById(id);
  }

  @Override
  public boolean update(Comment comment) {
    if (commentRepository.existsById(comment.getCommentId())) {
      commentRepository.save(comment);
      return true;
    }
    return false;
  }

  @Override
  public boolean delete(UUID id) {
    if (commentRepository.existsById(id)) {
      commentRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
