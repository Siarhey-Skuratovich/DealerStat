package com.leverx.dealerstat.mapping;

import com.leverx.dealerstat.model.Game;
import com.leverx.dealerstat.model.GameObject;
import com.leverx.dealerstat.model.Post;
import com.leverx.dealerstat.dto.creation.post.PostDto;
import com.leverx.dealerstat.service.serviceof.ServiceOf;
import com.leverx.dealerstat.service.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class PostMappingService {
  private final ServiceOf<Game> gameService;
  private final ServiceOf<GameObject> gameObjectService;
  private final UserService userService;

  public PostMappingService(ServiceOf<Game> gameService, ServiceOf<GameObject> gameObjectService, UserService userService) {
    this.gameService = gameService;
    this.gameObjectService = gameObjectService;
    this.userService = userService;
  }

  public Post mapFromDtoToPostEntity(PostDto postDto) {
    Post post = postDto.getPost();

    Game[] games = postDto.getGames();
    if (games != null) {
      for (Game game : games) {
        post.addGameTag(gameService.create(game));
      }
    }

    GameObject[] gameObjects = postDto.getGameObjects();
    if (gameObjects != null) {
      for (GameObject gameObject : gameObjects) {
        post.addGameObject(gameObjectService.create(gameObject));
      }
    }

    return post;
  }
}
