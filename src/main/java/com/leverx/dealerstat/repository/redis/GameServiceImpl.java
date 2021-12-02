package com.leverx.dealerstat.repository.redis;

import com.leverx.dealerstat.model.Game;
import com.leverx.dealerstat.repository.postgresql.GameRepository;
import com.leverx.dealerstat.service.serviceof.ServiceOf;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GameServiceImpl implements ServiceOf<Game> {
  private final GameRepository gameRepository;

  public GameServiceImpl(GameRepository gameRepository) {
    this.gameRepository = gameRepository;
  }

  @Override
  public void create(Game game) {
    gameRepository.save(game);
  }

  @Override
  public List<Game> readAll() {
    return gameRepository.findAll();
  }

  @Override
  public Game read(UUID id) {
    return gameRepository.getById(id);
  }

  @Override
  public boolean update(Game game) {
    if (gameRepository.existsById(game.getId())) {
      gameRepository.save(game);
      return true;
    }
    return false;
  }

  @Override
  public boolean delete(UUID id) {
    if (gameRepository.existsById(id)) {
      gameRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
