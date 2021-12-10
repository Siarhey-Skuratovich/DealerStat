package com.leverx.dealerstat.service.authorization;

import com.leverx.dealerstat.model.Comment;
import com.leverx.dealerstat.model.UserEntity;
import com.leverx.dealerstat.service.serviceof.ServiceOf;
import com.leverx.dealerstat.service.user.UserService;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class CommentAuthorshipVerifier {
  private final ServiceOf<Comment> commentService;
  private final UserService userService;

  public CommentAuthorshipVerifier(ServiceOf<Comment> commentService, UserService userService) {
    this.commentService = commentService;
    this.userService = userService;
  }

  public boolean hasAuthority(Principal principal, Comment comment) {
    UserEntity currentUser = userService.read(principal.getName());
    return comment.getAuthorId().equals(currentUser.getUserId());
  }
}

