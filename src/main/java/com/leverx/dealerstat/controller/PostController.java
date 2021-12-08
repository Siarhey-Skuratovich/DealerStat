package com.leverx.dealerstat.controller;

import com.leverx.dealerstat.model.Game;
import com.leverx.dealerstat.model.GameObject;
import com.leverx.dealerstat.model.Post;
import com.leverx.dealerstat.model.UserEntity;
import com.leverx.dealerstat.model.dto.creation.post.PostGameTagsAndGameObjects;
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
  private final ServiceOf<Game> gameService;
  private final ServiceOf<GameObject> gameObjectService;

  public PostController(ServiceOf<Post> postService, UserService userService, ServiceOf<Game> gameService, ServiceOf<GameObject> gameObjectService) {
    this.postService = postService;
    this.userService = userService;
    this.gameService = gameService;
    this.gameObjectService = gameObjectService;
  }

  @PostMapping(value = "/articles")
  public ResponseEntity<?> createPost(@RequestBody PostGameTagsAndGameObjects postGameTagsAndGameObjects, Principal principal) {
    Post post = postGameTagsAndGameObjects.getPost();
    UserEntity author = userService.read(principal.getName());
    post.setAuthorId(author.getUserId());
    post.setApproved(false);

    Game[] games = postGameTagsAndGameObjects.getGames();

    for (Game game : games) {
      post.getGames().add(gameService.create(game));
    }

    GameObject[] gameObjects = postGameTagsAndGameObjects.getGameObjects();

    for (GameObject gameObject : gameObjects) {
      gameObject.setAuthorId(author.getUserId());
      post.getGameObjects().add(gameObjectService.create(gameObject));
    }

    postService.create(post);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PatchMapping("/articles/{postId}")
  public ResponseEntity<?> addGameObjectsToThePost(@PathVariable UUID postId, @RequestBody GameObject[] gameObjects) {
    Post post = postService.read(postId);
    if (post == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    for (GameObject gameObject : gameObjects) {
      post.getGameObjects().add(gameObject);
    }

    postService.update(post);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/articles")
  public ResponseEntity<List<Post>> getAllApprovedPosts() {
    return new ResponseEntity<>(postService.readAll(), HttpStatus.OK);
  }

  /*@GetMapping("/articles/{postId}")
  public ResponseEntity<Post> getSpecifiedPost(@PathVariable UUID postId) {
    return new ResponseEntity<>(postService.read(postId), HttpStatus.OK);
  }*/

  @GetMapping(value = "users/{traderId}/articles")
  public ResponseEntity<Set<Post>> getPostsAboutTrader(@PathVariable UUID traderId) {
    UserEntity trader = userService.read(traderId);
    return new ResponseEntity<>(trader.getPosts(), HttpStatus.OK);
  }

  /*@GetMapping("/articles/{postId}/games")
  public ResponseEntity<Set<Game>> getGamesRelatedToThePost(@PathVariable UUID postId) {
    Post post = postService.read(postId);
    return new ResponseEntity<>(post.getGames(), HttpStatus.OK);
  }*/

  @GetMapping(value = "/my")
  public ResponseEntity<Set<Post>> getMyPosts(Principal principal) {
    UserEntity user = userService.read(principal.getName());
    return new ResponseEntity<>(user.getPosts(), HttpStatus.OK);
  }



}
