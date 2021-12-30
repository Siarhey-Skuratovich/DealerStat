package com.leverx.dealerstat.model;

import com.fasterxml.jackson.annotation.*;
import com.leverx.dealerstat.validation.groups.AdvancedInfo;
import com.leverx.dealerstat.validation.groups.InfoUserShouldPass;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "posts")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Post {
  @Id
  @Column(name = "post_id")
  @NotNull(groups = AdvancedInfo.class)
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
          name = "UUID",
          strategy = "org.hibernate.id.UUIDGenerator"
  )
  private UUID postId;

  @NotNull(message = "traderId cannot be null", groups = InfoUserShouldPass.class)
  @Column(name = "trader_id")
  @JoinColumn(name = "post_id", nullable = false)
  private UUID traderId;

  @NotBlank(message = "traderFirstName cannot be null, empty, blank", groups = InfoUserShouldPass.class)
  @Column(name = "trader_first_name")
  private String traderFirstName;

  @NotBlank(message = "traderLastName cannot be null, empty, blank", groups = InfoUserShouldPass.class)
  @Column(name = "trader_last_name")
  private String traderLastName;

  @NotNull(message = "authorId cannot be null", groups = AdvancedInfo.class)
  @Column(name = "author_id")
  private UUID authorId;

  @NotBlank(message = "text cannot be null, empty, blank", groups = InfoUserShouldPass.class)
  @Column(name = "text")
  private String text;

  @AssertFalse(groups = InfoUserShouldPass.class)
  @Column(name = "approved")
  private Boolean approved;

  @JsonIgnore
  @OneToMany(mappedBy = "postId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private Set<Comment> comments;

  @JsonIgnoreProperties({"posts", "gameObjects"})
  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinTable(
          name = "posts_games",
          joinColumns = @JoinColumn(name = "post_id"),
          inverseJoinColumns = @JoinColumn(name = "game_id")
  )
  Set<Game> games = new HashSet<>();

  @JsonIgnoreProperties(value = "posts")
  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinTable(
          name = "posts_game_objects",
          joinColumns = @JoinColumn(name = "post_id"),
          inverseJoinColumns = @JoinColumn(name = "game_object_id")
  )
  Set<GameObject> gameObjects = new HashSet<>();

  public void addGameTag(Game game) {
    games.add(game);
  }

  public void addGameObject(GameObject gameObject) {
    gameObjects.add(gameObject);
  }
}
