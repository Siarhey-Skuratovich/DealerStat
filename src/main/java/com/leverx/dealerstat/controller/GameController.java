package com.leverx.dealerstat.controller;

import com.leverx.dealerstat.model.Game;
import com.leverx.dealerstat.model.Post;
import com.leverx.dealerstat.service.serviceof.ServiceOf;
import com.leverx.dealerstat.validation.groups.InfoUserShouldPass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class GameController {
  private final ServiceOf<Game> gameService;

  public GameController(ServiceOf<Game> gameService) {
    this.gameService = gameService;
  }

  @PostMapping(value = "/games")
  public ResponseEntity<?> createGame(@Validated(InfoUserShouldPass.class) @RequestBody Game game) {
    gameService.create(game);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping(value = " /games/{gameId}")
  public ResponseEntity<?> updateGame(
          @Validated(InfoUserShouldPass.class)
          @PathVariable UUID gameId,
          @RequestBody Game updatedGame) {

    updatedGame.setGameId(gameId);

    if (gameService.update(updatedGame)) {
      return  new ResponseEntity<>(HttpStatus.OK);
    }
    return new ResponseEntity<> (HttpStatus.NOT_FOUND);
  }

  @GetMapping(value = "/games")
  public ResponseEntity<List<Game>> getAllGames() {
    return new ResponseEntity<>(gameService.readAll(), HttpStatus.OK);
  }

  @GetMapping("/games/{gameId}/posts")
  public ResponseEntity<Set<Post>> getPostsRelatedToTheGame(@PathVariable UUID gameId) {
    Optional<Game> gameOptional = gameService.read(gameId);

    if (gameOptional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    Set<Post> approvedPosts = gameOptional.get().getPosts().stream()
            .filter(Post::getApproved)
            .collect(Collectors.toSet());

    return new ResponseEntity<>(approvedPosts, HttpStatus.OK);
  }
}
