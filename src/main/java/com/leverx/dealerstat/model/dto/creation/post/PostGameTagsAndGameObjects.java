package com.leverx.dealerstat.model.dto.creation.post;

import com.leverx.dealerstat.model.Game;
import com.leverx.dealerstat.model.GameObject;
import com.leverx.dealerstat.model.Post;
import lombok.Getter;

@Getter
public class PostGameTagsAndGameObjects {
  private Post post;
  private Game[] games;
  private GameObject[] gameObjects;
}
