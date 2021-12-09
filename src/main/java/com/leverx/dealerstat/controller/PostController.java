package com.leverx.dealerstat.controller;

import com.leverx.dealerstat.config.HibernateUtil;
import com.leverx.dealerstat.mapping.PostMappingService;
import com.leverx.dealerstat.model.GameObject;
import com.leverx.dealerstat.model.Post;
import com.leverx.dealerstat.model.UserEntity;
import com.leverx.dealerstat.model.dto.creation.post.PostDto;
import com.leverx.dealerstat.service.serviceof.ServiceOf;
import com.leverx.dealerstat.service.user.UserService;
import com.leverx.dealerstat.validation.groups.InfoUserShouldPass;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@RestController
public class PostController {
  private final ServiceOf<Post> postService;
  private final UserService userService;
  private final PostMappingService postMappingService;

  public PostController(ServiceOf<Post> postService, UserService userService, PostMappingService postMappingService) {
    this.postService = postService;
    this.userService = userService;
    this.postMappingService = postMappingService;
  }

  @PostMapping(value = "/articles")
  public ResponseEntity<?> createPost(@Validated(InfoUserShouldPass.class) @RequestBody PostDto postDto) {
    Post post = postMappingService.mapFromDtoToPostEntity(postDto);

    postService.create(post);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PatchMapping("/articles/{postId}")
  public ResponseEntity<?> addGameObjectToThePost(@PathVariable UUID postId,
                                                  @Validated(InfoUserShouldPass.class) @RequestBody GameObject gameObject) {
    Session session = HibernateUtil.getSessionFactory().openSession();
    Post post = session.get(Post.class, postId);
    session.close();

    if (post == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    post.addGameObject(gameObject);

    postService.update(post);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/articles")
  public ResponseEntity<List<Post>> getAllApprovedPosts() {
    return new ResponseEntity<>(postService.readAll(), HttpStatus.OK);
  }

  @GetMapping(value = "users/{traderId}/articles")
  public ResponseEntity<Set<Post>> getPostsAboutTrader(@PathVariable UUID traderId) {

    if (userService.noUserById(traderId)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    UserEntity trader = userService.read(traderId);
    return new ResponseEntity<>(trader.getPosts(), HttpStatus.OK);
  }

  @GetMapping(value = "/my")
  public ResponseEntity<Set<Post>> getMyPosts(Principal principal) {
    UserEntity user = userService.read(principal.getName());
    return new ResponseEntity<>(user.getPosts(), HttpStatus.OK);
  }

  /*@GetMapping("/articles/{postId}")
  public ResponseEntity<Post> getSpecifiedPost(@PathVariable UUID postId) {
    return new ResponseEntity<>(postService.read(postId), HttpStatus.OK);
  }*/

    /*@GetMapping("/articles/{postId}/games")
  public ResponseEntity<Set<Game>> getGamesRelatedToThePost(@PathVariable UUID postId) {
    Post post = postService.read(postId);
    return new ResponseEntity<>(post.getGames(), HttpStatus.OK);
  }*/
}
