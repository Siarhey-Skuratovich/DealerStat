package com.leverx.dealerstat.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "game_objects")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class GameObject {
  @Id
  @Column(name = "game_object_id")
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
          name = "UUID",
          strategy = "org.hibernate.id.UUIDGenerator"
  )
  private UUID gameObjectId;

  @Column(name = "title")
  private String title;

  @Column(name = "text")
  private String text;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "author_id")
  private UUID authorId;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "game_id")
  @JoinColumn(name="game_object_id", nullable=false)
  private UUID gameId;

  @JsonIgnoreProperties(value = "gameObjects")
  @ManyToMany(mappedBy = "gameObjects", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  Set<Post> posts;

  public enum Status {
    AVAILABLE,
    SOLD
  }
}
