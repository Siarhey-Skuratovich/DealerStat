package com.leverx.dealerstat.controller;

import com.leverx.dealerstat.model.Comment;
import com.leverx.dealerstat.model.UserEntity;
import com.leverx.dealerstat.service.commentservice.CommentService;
import com.leverx.dealerstat.service.userservice.UserService;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
public class CommentController {
  private final CommentService commentService;
  private final UserService userService;

  public CommentController(CommentService commentService, UserService userService) {
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
    ////////////////////
    ////////////////////
    ///////////////////temporary
    return new ResponseEntity<List<Comment>>(commentService.readAll(), HttpStatus.OK);
  }
}
