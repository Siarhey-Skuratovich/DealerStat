package com.leverx.dealerstat.service.serviceof.impl;

import com.leverx.dealerstat.config.WebSecurityConfig;
import com.leverx.dealerstat.model.Comment;
import com.leverx.dealerstat.model.UserEntity;
import com.leverx.dealerstat.repository.postgresql.CommentRepository;
import com.leverx.dealerstat.service.serviceof.ServiceOf;
import com.leverx.dealerstat.service.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService implements ServiceOf<Comment> {
  private final CommentRepository commentRepository;
  private final UserService userService;

  public CommentService(CommentRepository commentRepository, UserService userService) {
    this.commentRepository = commentRepository;
    this.userService = userService;
  }

  @Override
  public Comment create(Comment comment) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserEntity author = userService.read(authentication.getName());
    comment.setAuthorId(author.getUserId());

    comment.setCreatedAt(LocalDateTime.now());

    comment.setApproved(false);

    return commentRepository.save(comment);
  }

  @Override
  public List<Comment> readAll() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication.getName().equals(WebSecurityConfig.ADMIN_USERNAME)) {
      return commentRepository.findAll();
    }

    return commentRepository.findByApproved(true);
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

  @Override
  public boolean notContainsById(UUID id) {
    return !commentRepository.existsById(id);
  }
}
