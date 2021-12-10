package com.leverx.dealerstat.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "games")
@Component
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Game {
  @Id
  @Column(name = "game_id")
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
          name = "UUID",
          strategy = "org.hibernate.id.UUIDGenerator"
  )
  private UUID gameId;

  @NotBlank(message = "name cannot be null, empty, blank")
  @Column(name = "game_name")
  private String name;

  @JsonIgnoreProperties(value = "games")
  @ManyToMany(mappedBy = "games", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  Set<Post> posts = new HashSet<>();

  @JsonIgnoreProperties(value = "posts")
  @OneToMany(mappedBy = "gameId", cascade = CascadeType.ALL, orphanRemoval = true)
  Set<GameObject> gameObjects = new HashSet<>();
}
