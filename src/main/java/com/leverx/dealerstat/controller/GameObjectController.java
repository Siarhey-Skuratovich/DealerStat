package com.leverx.dealerstat.controller;

import com.leverx.dealerstat.model.GameObject;
import com.leverx.dealerstat.service.serviceof.ServiceOf;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameObjectController {
  private ServiceOf<GameObject> gameObjectService;

  public GameObjectController(ServiceOf<GameObject> gameObjectService) {
    this.gameObjectService = gameObjectService;
  }

  @PostMapping(value = " /objects")
  public ResponseEntity<?> createGameObject(@RequestBody GameObject gameObject) {
    gameObjectService.create(gameObject);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
