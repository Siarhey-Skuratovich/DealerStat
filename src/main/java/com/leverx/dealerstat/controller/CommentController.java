package com.leverx.dealerstat.controller;

import com.leverx.dealerstat.model.Comment;
import com.leverx.dealerstat.model.Post;
import com.leverx.dealerstat.model.UserEntity;
import com.leverx.dealerstat.service.serviceof.ServiceOf;
import com.leverx.dealerstat.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
public class CommentController {
  private final ServiceOf<Comment> commentService;
  private final ServiceOf<Post> postService;
  private final UserService userService;

  public CommentController(ServiceOf<Comment> commentService, ServiceOf<Post> postService, UserService userService) {
    this.commentService = commentService;
    this.postService = postService;
    this.userService = userService;
  }

  @PostMapping(value = "/articles/{postId}/comments")
  public ResponseEntity<?> addComment(@RequestBody Comment comment, @PathVariable UUID postId, Principal principal) {
    UserEntity author = userService.read(principal.getName());
    comment.setAuthorId(author.getUserId());
    comment.setPostId(postId);
    commentService.create(comment);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping(value = "/articles/{postId}/comments")
  public ResponseEntity<Set<Comment>> getCommentsOfPost(@PathVariable UUID postId) {
    Post post = postService.read(postId);
    return new ResponseEntity<>(post.getComments(), HttpStatus.OK);
  }

  @GetMapping(value = "/users/{traderId}/comments")
  public ResponseEntity<Set<Comment>> getAllCommentsRelatedToTheTrader(@PathVariable UUID traderId) {
    UserEntity trader = userService.read(traderId);
    Set<Post> postsRelatedToTheTrader = trader.getPosts();
    Set<Comment> comments = postsRelatedToTheTrader.stream()
            .map(Post::getComments)
            .flatMap(Collection::parallelStream)
            .collect(Collectors.toSet());
    return new ResponseEntity<>(comments, HttpStatus.OK);
  }

  @GetMapping(value = "/users/{traderId}/comments/{commentId}")
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
  }
}

