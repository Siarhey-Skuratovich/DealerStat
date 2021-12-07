package com.leverx.dealerstat.model.dto;

import com.leverx.dealerstat.model.Game;
import com.leverx.dealerstat.model.Post;
import lombok.Getter;

@Getter
public class PostAndGames {
  private Post post;
  private Game[] games;
}
