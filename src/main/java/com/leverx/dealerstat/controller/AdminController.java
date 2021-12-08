package com.leverx.dealerstat.controller;

import com.leverx.dealerstat.model.Comment;
import com.leverx.dealerstat.model.ConfirmationUserCode;
import com.leverx.dealerstat.model.Post;
import com.leverx.dealerstat.model.UserEntity;
import com.leverx.dealerstat.service.confirmatiocode.ConfirmationCodeService;
import com.leverx.dealerstat.service.serviceof.ServiceOf;
import com.leverx.dealerstat.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/***REMOVED***istration")
public class AdminController {
  private final ConfirmationCodeService confirmationCodeService;
  private final UserService userService;
  private final ServiceOf<Post> postService;
  private final ServiceOf<Comment> commentService;

  public AdminController(ConfirmationCodeService confirmationCodeService, UserService userService, ServiceOf<Post> postService, ServiceOf<Comment> commentService) {
    this.confirmationCodeService = confirmationCodeService;
    this.userService = userService;
    this.postService = postService;
    this.commentService = commentService;
  }

  @GetMapping(value = "/codes")
  public ResponseEntity<List<ConfirmationUserCode>> readCodes() {
    final List<ConfirmationUserCode> codes = confirmationCodeService.readAll();
    return codes != null && !codes.isEmpty()
            ? new ResponseEntity<>(codes, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @GetMapping(value = "/users")
  public ResponseEntity<List<UserEntity>> readUsers() {
    final List<UserEntity> userEntities = userService.readAll();
    return userEntities != null && !userEntities.isEmpty()
            ? new ResponseEntity<>(userEntities, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @GetMapping("/articles")
  public ResponseEntity<List<Post>> getAllPosts() {
    return new ResponseEntity<>(postService.readAll(), HttpStatus.OK);
  }

  @GetMapping("/comments")
  public ResponseEntity<List<Comment>> getAllComments() {
    return new ResponseEntity<>(commentService.readAll(), HttpStatus.OK);
  }

  @PatchMapping("articles/{postId}")
  public ResponseEntity<?> approvePost(@PathVariable UUID postId) {
    Post post = postService.read(postId);
    if (post == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    post.setApproved(true);
    postService.update(post);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PatchMapping("comments/{commentId}")
  public ResponseEntity<?> approveComment(@PathVariable UUID commentId) {
    Comment comment = commentService.read(commentId);
    if (comment == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    comment.setApproved(true);
    commentService.update(comment);

    return new ResponseEntity<>(HttpStatus.OK);
  }

}
