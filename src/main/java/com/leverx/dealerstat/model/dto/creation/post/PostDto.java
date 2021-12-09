package com.leverx.dealerstat.model.dto.creation.post;

import com.leverx.dealerstat.model.Game;
import com.leverx.dealerstat.model.GameObject;
import com.leverx.dealerstat.model.Post;
import lombok.Getter;

import javax.validation.Valid;

@Getter
public class PostDto {
  @Valid
  private Post post;

  @Valid
  private Game[] games;

  @Valid
  private GameObject[] gameObjects;
}
