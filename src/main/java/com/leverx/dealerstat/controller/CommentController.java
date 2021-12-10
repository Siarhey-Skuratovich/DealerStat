package com.leverx.dealerstat.controller;

import com.leverx.dealerstat.service.authorization.CommentAuthorshipVerifier;
import com.leverx.dealerstat.model.Comment;
import com.leverx.dealerstat.model.Post;
import com.leverx.dealerstat.model.UserEntity;
import com.leverx.dealerstat.dto.updation.comment.UpdatedCommentMessageDto;
import com.leverx.dealerstat.service.serviceof.ServiceOf;
import com.leverx.dealerstat.service.user.UserService;
import com.leverx.dealerstat.validation.groups.InfoUserShouldPass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Transactional
public class CommentController {
  private final ServiceOf<Comment> commentService;
  private final ServiceOf<Post> postService;
  private final UserService userService;
  private final CommentAuthorshipVerifier commentAuthorshipVerifier;

  public CommentController(ServiceOf<Comment> commentService, ServiceOf<Post> postService, UserService userService, CommentAuthorshipVerifier commentAuthorshipVerifier) {
    this.commentService = commentService;
    this.postService = postService;
    this.userService = userService;
    this.commentAuthorshipVerifier = commentAuthorshipVerifier;
  }

  @PostMapping(value = "/articles/{postId}/comments")
  public ResponseEntity<?> addComment(@Validated(InfoUserShouldPass.class)
                                      @RequestBody Comment comment,
                                      @PathVariable UUID postId) {
    Optional<Post> existedPost = postService.read(postId);
    if (existedPost.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    comment.setPostId(postId);

    commentService.create(comment);

    return ResponseEntity.ok().build();
  }

  @GetMapping(value = "/articles/{postId}/comments")
  public ResponseEntity<Set<Comment>> getCommentsOfPost(@PathVariable UUID postId) {
    Optional<Post> optionalPost = postService.read(postId);

    if (optionalPost.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    Set<Comment> approvedComments = optionalPost.get().getComments()
            .stream()
            .filter(Comment::getApproved)
            .collect(Collectors.toSet());

    return new ResponseEntity<>(approvedComments, HttpStatus.OK);
  }

  @GetMapping(value = "/users/{traderId}/comments")
  public ResponseEntity<Set<Comment>> getApprovedCommentsRelatedToTheTrader(@PathVariable UUID traderId) {
    Optional<UserEntity> traderOptional = userService.read(traderId);

    if (traderOptional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    Set<Post> postsRelatedToTheTrader = traderOptional.get().getPosts();

    Set<Comment> comments = postsRelatedToTheTrader
            .stream()
            .map(Post::getComments)
            .flatMap(Collection::parallelStream)
            .filter(Comment::getApproved)
            .collect(Collectors.toSet());

    return new ResponseEntity<>(comments, HttpStatus.OK);
  }

  @GetMapping("/comments/{commentId}")
  public ResponseEntity<Comment> getSpecificComment(@PathVariable UUID commentId) {
    Optional<Comment> optionalComment = commentService.read(commentId);

    if (optionalComment.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return new ResponseEntity<>(optionalComment.get(), HttpStatus.OK);
  }

  @DeleteMapping("comments/{commentId}")
  public ResponseEntity<Comment> deleteComment(@PathVariable UUID commentId, Principal principal) {

    Optional<Comment> commentOptional = commentService.read(commentId);
    if (commentOptional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    if (commentAuthorshipVerifier.hasAuthority(principal, commentOptional.get())) {
      commentService.delete(commentId);
      return ResponseEntity.ok().build();
    }

    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
  }

  @PutMapping("/comments/{commentId}")
  public ResponseEntity<Comment> updateComment(@PathVariable UUID commentId,
                                               @Valid
                                               @RequestBody UpdatedCommentMessageDto updatedCommentMessageDto, Principal principal) {
    Optional<Comment> commentOptional = commentService.read(commentId);
    if (commentOptional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    if (commentAuthorshipVerifier.hasAuthority(principal, commentOptional.get())) {
      Comment commentToUpdate = commentOptional.get();
      commentToUpdate.setMessage(updatedCommentMessageDto.getMessage());

      commentService.update(commentToUpdate);

      return new ResponseEntity<>(HttpStatus.OK);
    }

    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
  }

  @GetMapping("/comments")
  public ResponseEntity<List<Comment>> getApprovedComments() {
    return new ResponseEntity<>(commentService.readAll(), HttpStatus.OK);
  }

    /*@GetMapping(value = "/users/{traderId}/comments/{commentId}")
  public ResponseEntity<Comment> getSpecificCommentRelatedToTheTrader(@PathVariable UUID traderId, @PathVariable UUID commentId) {
    UserEntity trader = userService.read(traderId);
    Set<Post> postsRelatedToTheTrader = trader.getPosts();
    Optional<Comment> specificComment = postsRelatedToTheTrader.stream()
            .map(Post::getComments)
            .flatMap(Collection::parallelStream)
            .filter(comment -> comment.getCommentId().equals(commentId))
            .findFirst();
    return specificComment
            .map(comment -> new ResponseEntity<>(comment, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }*/
}

