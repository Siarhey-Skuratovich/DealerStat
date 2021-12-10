package com.leverx.dealerstat.service.authorization;

import com.leverx.dealerstat.model.GameObject;
import com.leverx.dealerstat.model.UserEntity;
import com.leverx.dealerstat.service.serviceof.ServiceOf;
import com.leverx.dealerstat.service.user.UserService;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class GameObjectAuthorshipVerifier {
  private final ServiceOf<GameObject> gameObjectService;
  private final UserService userService;

  public GameObjectAuthorshipVerifier(ServiceOf<GameObject> gameObjectService, UserService userService) {
    this.gameObjectService = gameObjectService;
    this.userService = userService;
  }

  public boolean hasAuthority(Principal principal, GameObject gameObject) {
    UserEntity currentUser = userService.read(principal.getName());
    return gameObject.getAuthorId().equals(currentUser.getUserId());
  }
}
