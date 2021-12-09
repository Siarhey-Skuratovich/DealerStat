package com.leverx.dealerstat.service.serviceof.impl;

import com.leverx.dealerstat.model.GameObject;
import com.leverx.dealerstat.model.UserEntity;
import com.leverx.dealerstat.repository.postgresql.GameObjectRepository;
import com.leverx.dealerstat.service.serviceof.ServiceOf;
import com.leverx.dealerstat.service.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class GameObjectService implements ServiceOf<GameObject> {
  private final GameObjectRepository gameObjectRepository;
  private final UserService userService;

  public GameObjectService(GameObjectRepository gameObjectRepository, UserService userService) {
    this.gameObjectRepository = gameObjectRepository;
    this.userService = userService;
  }

  @Override
  public GameObject create(GameObject gameObject) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserEntity author = userService.read(authentication.getName());
    gameObject.setAuthorId(author.getUserId());

    gameObject.setStatus(GameObject.Status.AVAILABLE);
    gameObject.setCreatedAt(LocalDateTime.now());
    gameObject.setUpdatedAt(LocalDateTime.now());
    return gameObjectRepository.save(gameObject);
  }

  @Override
  public List<GameObject> readAll() {
    return gameObjectRepository.findAll();
  }

  @Override
  public GameObject read(UUID id) {
    return gameObjectRepository.getById(id);
  }

  @Override
  public boolean update(GameObject gameObject) {
    if (gameObjectRepository.existsById(gameObject.getGameObjectId())) {
      gameObjectRepository.save(gameObject);
      return true;
    }
    return false;
  }

  @Override
  public boolean delete(UUID id) {
    if (gameObjectRepository.existsById(id)) {
      gameObjectRepository.deleteById(id);
      return true;
    }
    return false;
  }

  @Override
  public boolean notContainsById(UUID id) {
    return !gameObjectRepository.existsById(id);
  }
}
