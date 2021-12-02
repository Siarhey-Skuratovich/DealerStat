package com.leverx.dealerstat.controller;

import com.leverx.dealerstat.model.Comment;
import com.leverx.dealerstat.model.UserEntity;
import com.leverx.dealerstat.service.serviceof.ServiceOf;
import com.leverx.dealerstat.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
public class CommentController {
  private final ServiceOf<Comment> commentService;
  private final UserService userService;

  public CommentController(ServiceOf<Comment> commentService, UserService userService) {
    this.commentService = commentService;
    this.userService = userService;
  }

  @PostMapping(value = "/articles/{postId}/comments")
  public ResponseEntity<?> addComment(@RequestBody Comment comment, @PathVariable UUID postId, Principal principal) {
    UserEntity author = userService.read(principal.getName());
    comment.setAuthorId(author.getId());
    comment.setPostId(postId);
    commentService.create(comment);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping(value = "/users/{traderId}/comments")
  public ResponseEntity<List<Comment>> getComments(@PathVariable UUID traderId) {



    return new ResponseEntity<List<Comment>>(commentService.readAll(), HttpStatus.OK);
  }
}
