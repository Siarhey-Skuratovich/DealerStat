package com.leverx.dealerstat.controller;

import com.leverx.dealerstat.config.HibernateUtil;
import com.leverx.dealerstat.model.Game;
import com.leverx.dealerstat.model.Post;
import com.leverx.dealerstat.model.UserEntity;
import com.leverx.dealerstat.model.dto.PostAndGames;
import com.leverx.dealerstat.service.serviceof.ServiceOf;
import com.leverx.dealerstat.service.user.UserService;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.enterprise.inject.Produces;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@RestController
public class PostController {
  private final ServiceOf<Post> postService;
  private final UserService userService;
  private final ServiceOf<Game> gameService;

  public PostController(ServiceOf<Post> postService, UserService userService, ServiceOf<Game> gameService) {
    this.postService = postService;
    this.userService = userService;
    this.gameService = gameService;
  }

  @PostMapping(value = "/articles")
  public ResponseEntity<?> createPost(@RequestBody PostAndGames postAndGames, Principal principal) {
    Post post = postAndGames.getPost();
    UserEntity author = userService.read(principal.getName());
    post.setAuthorId(author.getUserId());
    post.setApproved(false);

    Game[] games = postAndGames.getGames();

    Session session = HibernateUtil.getSessionFactory().openSession();
    session.beginTransaction();

    for (Game game : games) {
      post.getGames().add(game);
    }

    session.persist(post);

    session.flush();
    session.getTransaction().commit();

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/articles")
  public ResponseEntity<List<Post>> getAllPosts() {
    return new ResponseEntity<>(postService.readAll(), HttpStatus.OK);
  }

  @GetMapping("/articles/{postId}")
  public ResponseEntity<Post> getSpecifiedPost(@PathVariable UUID postId) {
    return new ResponseEntity<>(postService.read(postId), HttpStatus.OK);
  }

  @GetMapping(value = "users/{traderId}/articles")
  public ResponseEntity<Set<Post>> getPostsAboutTrader(@PathVariable UUID traderId) {
    UserEntity trader = userService.read(traderId);
    return new ResponseEntity<>(trader.getPosts(), HttpStatus.OK);
  }

  @GetMapping("/articles/{postId}/games")
  public ResponseEntity<Set<Game>> getGamesRelatedToThePost(@PathVariable UUID postId) {
    Post post = postService.read(postId);
    return new ResponseEntity<>(post.getGames(), HttpStatus.OK);
  }
}
