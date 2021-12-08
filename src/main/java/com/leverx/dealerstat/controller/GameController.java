package com.leverx.dealerstat.controller;

import com.leverx.dealerstat.model.Game;
import com.leverx.dealerstat.service.serviceof.ServiceOf;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class GameController {
  private final ServiceOf<Game> gameService;

  public GameController(ServiceOf<Game> gameService) {
    this.gameService = gameService;
  }

  @PostMapping(value = "/games")
  public ResponseEntity<?> createGame(@RequestBody Game game) {
    gameService.create(game);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping(value = " /games/{gameId}")
  public ResponseEntity<?> updateGame(@PathVariable UUID gameId, @RequestBody Game updatedGame) {

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

  /*@GetMapping("/games/{gameId}/posts")
  public ResponseEntity<Set<Post>> getPostsRelatedToTheGame(@PathVariable UUID gameId) {
    Game game = gameService.read(gameId);
    return new ResponseEntity<>(game.getPosts(), HttpStatus.OK);
  }*/
}
