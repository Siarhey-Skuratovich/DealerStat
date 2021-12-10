package com.leverx.dealerstat.controller;

import com.leverx.dealerstat.model.Game;
import com.leverx.dealerstat.model.GameObject;
import com.leverx.dealerstat.model.UserEntity;
import com.leverx.dealerstat.service.serviceof.ServiceOf;
import com.leverx.dealerstat.service.user.UserService;
import com.leverx.dealerstat.validation.groups.InfoUserShouldPass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class GameObjectController {
  private final ServiceOf<GameObject> gameObjectService;
  private final UserService userService;
  private final ServiceOf<Game> gameService;

  public GameObjectController(ServiceOf<GameObject> gameObjectService, UserService userService, ServiceOf<Game> gameService) {
    this.gameObjectService = gameObjectService;
    this.userService = userService;
    this.gameService = gameService;
  }

  @PostMapping(value = "/objects")
  public ResponseEntity<?> createGameObject(@Validated(InfoUserShouldPass.class) @RequestBody GameObject gameObject) {

    Optional<Game> gameOptional = gameService.read(gameObject.getGameId());
    if (gameOptional.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    gameObjectService.create(gameObject);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping(value = "/objects/{gameObjectId}")
  public ResponseEntity<?> updateGameObject(
          @PathVariable UUID gameObjectId,
          @Validated(InfoUserShouldPass.class)
          @RequestBody GameObject updatedGameObject) {


    updatedGameObject.setGameObjectId(gameObjectId);

    if (gameObjectService.update(updatedGameObject)) {
      return new ResponseEntity<>(HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @GetMapping(value = "/objects")
  public ResponseEntity<List<GameObject>> getGameObjects() {
    return new ResponseEntity<>(gameObjectService.readAll(), HttpStatus.OK);
  }

  @DeleteMapping(value = "/objects/{gameObjectId}")
  public ResponseEntity<?> deleteGameObject(@PathVariable UUID gameObjectId, Principal principal) {
    UserEntity currentUser = userService.read(principal.getName());
    Optional<GameObject> gameObjectOptional = gameObjectService.read(gameObjectId);

    if (gameObjectOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    if (gameObjectOptional.get().getAuthorId().equals(currentUser.getUserId())) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    gameObjectService.delete(gameObjectId);

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
