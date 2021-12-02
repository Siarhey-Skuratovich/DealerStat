package com.leverx.dealerstat.controller;

import com.leverx.dealerstat.model.Post;
import com.leverx.dealerstat.model.UserEntity;
import com.leverx.dealerstat.service.serviceof.ServiceOf;
import com.leverx.dealerstat.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@RestController
public class PostController {
  private final ServiceOf<Post> postService;
  private final UserService userService;

  public PostController(ServiceOf<Post> postService, UserService userService) {
    this.postService = postService;
    this.userService = userService;
  }

  @PostMapping(value = "/articles")
  public ResponseEntity<?> createPost(@RequestBody Post post, Principal principal) {
    UserEntity author = userService.read(principal.getName());
    post.setAuthorId(author.getId());
    post.setApproved(false);
    postService.create(post);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/articles")
  public ResponseEntity<List<Post>> getAllPosts() {
    return new ResponseEntity<>(postService.readAll(), HttpStatus.OK);
  }

  @GetMapping(value = "users/{traderId}/articles")
  public ResponseEntity<Set<Post>> getPostsAboutTrader(@PathVariable UUID traderId) {
    UserEntity trader = userService.read(traderId);
    return new ResponseEntity<>(trader.getPosts(), HttpStatus.OK);
  }
}
