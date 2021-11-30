package com.leverx.dealerstat.service.commentservice;

import com.leverx.dealerstat.model.Comment;
import com.leverx.dealerstat.repository.postgresql.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CommentServiceImpl implements CommentService {
  private final CommentRepository commentRepository;

  public CommentServiceImpl(CommentRepository commentRepository) {
    this.commentRepository = commentRepository;
  }

  @Override
  public void create(Comment comment) {
    comment.setId(UUID.randomUUID());
    comment.setCreatedAt(LocalDateTime.now());
    comment.setApproved(false);
    commentRepository.save(comment);
  }

  @Override
  public List<Comment> readAll() {
    return commentRepository.findAll();
  }

  @Override
  public Comment read(UUID id) {
    return null;
  }

  @Override
  public boolean update(Comment comment) {
    return false;
  }

  @Override
  public boolean delete(UUID id) {
    return false;
  }
}
