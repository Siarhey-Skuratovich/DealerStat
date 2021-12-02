package com.leverx.dealerstat.service.serviceof.impl;

import com.leverx.dealerstat.model.GameObject;
import com.leverx.dealerstat.repository.postgresql.GameObjectRepository;
import com.leverx.dealerstat.service.serviceof.ServiceOf;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GameObjectService implements ServiceOf<GameObject> {
  private final GameObjectRepository gameObjectRepository;

  public GameObjectService(GameObjectRepository gameObjectRepository) {
    this.gameObjectRepository = gameObjectRepository;
  }

  @Override
  public void create(GameObject gameObject) {
    gameObjectRepository.save(gameObject);
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
}
