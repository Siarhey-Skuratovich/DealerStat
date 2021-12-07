package com.leverx.dealerstat.model;

import com.fasterxml.jackson.annotation.*;;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
          name = "UUID",
          strategy = "org.hibernate.id.UUIDGenerator"
  )
  private UUID postId;

  @Column(name = "trader_id")
  @JoinColumn(name = "post_id", nullable = false)
  private UUID traderId;

  @Column(name = "trader_first_name")
  private String traderFirstName;

  @Column(name = "trader_last_name")
  private String traderLastName;

  @Column(name = "author_id")
  private UUID authorId;

  @Column(name = "text")
  private String text;

  @Column(name = "approved")
  private Boolean approved;

  @OneToMany(mappedBy = "postId", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Comment> comments;

  //  public void removeComment(Comment comment) {
//    comments.remove(comment);
//    comment.setPostId(null);
//  }

  @JsonIgnore
  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinTable(
          name = "posts_games",
          joinColumns = @JoinColumn(name = "post_id"),
          inverseJoinColumns = @JoinColumn(name = "game_id")
  )
  Set<Game> games = new HashSet<>();

  @JsonIgnore
  @ManyToMany
  @JoinTable(
          name = "posts_game_objects",
          joinColumns = @JoinColumn(name = "post_id"),
          inverseJoinColumns = @JoinColumn(name = "game_object_id")
  )
  Set<GameObject> gameObjects = new HashSet<>();
}
