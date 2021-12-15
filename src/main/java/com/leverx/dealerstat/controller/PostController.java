package com.leverx.dealerstat.controller;

import com.leverx.dealerstat.mapping.PostMappingService;
import com.leverx.dealerstat.model.GameObject;
import com.leverx.dealerstat.model.Post;
import com.leverx.dealerstat.model.UserEntity;
import com.leverx.dealerstat.dto.creation.post.PostDto;
import com.leverx.dealerstat.service.serviceof.ServiceOf;
import com.leverx.dealerstat.service.user.UserService;
import com.leverx.dealerstat.validation.groups.InfoUserShouldPass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
public class PostController {
  private final ServiceOf<Post> postService;
  private final UserService userService;
  private final PostMappingService postMappingService;
  private final ServiceOf<GameObject> gameObjectService;

  public PostController(ServiceOf<Post> postService, UserService userService, PostMappingService postMappingService, ServiceOf<GameObject> gameObjectService) {
    this.postService = postService;
    this.userService = userService;
    this.postMappingService = postMappingService;
    this.gameObjectService = gameObjectService;
  }

  @PostMapping(value = "/articles")
  public ResponseEntity<?> createPost(@Validated(InfoUserShouldPass.class) @RequestBody PostDto postDto) {

    Optional<UserEntity> optionalRelatedTrader = userService.read(postDto.getPost().getTraderId());
    if (optionalRelatedTrader.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    Post post = postMappingService.mapFromDtoToPostEntity(postDto);

    postService.create(post);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PatchMapping("/articles/{postId}")
  public ResponseEntity<?> addGameObjectToThePost(@PathVariable UUID postId,
                                                  @Validated(InfoUserShouldPass.class) @RequestBody GameObject gameObject) {

    Optional<Post> postOptional = postService.read(postId);

    if (postOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    if (gameObject.getGameObjectId() == null) {
      gameObject = gameObjectService.create(gameObject);
    }


    Post post = postOptional.get();
    post.addGameObject(gameObject);

    postService.update(post);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/articles")
  public ResponseEntity<List<Post>> getAllApprovedPosts() {
    return new ResponseEntity<>(postService.readAll(), HttpStatus.OK);
  }

  @GetMapping(value = "users/{traderId}/articles")
  public ResponseEntity<Set<Post>> getApprovedPostsAboutTrader(@PathVariable UUID traderId) {

    Optional<UserEntity> traderOptional = userService.read(traderId);

    if (traderOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    Set<Post> approvedPosts = traderOptional.get().getPosts()
            .stream()
            .filter(Post::getApproved)
            .collect(Collectors.toSet());

    return new ResponseEntity<>(approvedPosts, HttpStatus.OK);
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
