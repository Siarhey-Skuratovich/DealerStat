package com.leverx.dealerstat.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
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
  private UUID id;

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
  private UUID gameId;

  /*@ManyToMany(mappedBy = "gameObjects")
  Set<Post> posts;*/

  public enum Status {
    AVAILABLE,
    SOLD
  }
}
