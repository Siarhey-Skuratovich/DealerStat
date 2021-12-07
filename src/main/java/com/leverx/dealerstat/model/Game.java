package com.leverx.dealerstat.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "games")
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

  @Column(name = "game_name")
  private String name;

  @JsonIgnoreProperties(value = "games")
  @ManyToMany(mappedBy = "games", fetch = FetchType.LAZY)
  Set<Post> posts = new HashSet<>();
}
