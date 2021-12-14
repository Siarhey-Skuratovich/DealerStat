package com.leverx.dealerstat.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.leverx.dealerstat.validation.groups.AdvancedInfo;
import com.leverx.dealerstat.validation.groups.InfoUserShouldPass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

  @NotBlank(message = "title cannot be null, empty, blank", groups = InfoUserShouldPass.class)
  @Column(name = "title")
  private String title;

  @NotBlank(message = "text cannot be null, empty, blank", groups = InfoUserShouldPass.class)
  @Column(name = "text")
  private String text;

  @NotNull(message = "status cannot be null", groups = AdvancedInfo.class)
  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;

  @NotNull(message = "authorId cannot be null", groups = AdvancedInfo.class)
  @Column(name = "author_id")
  private UUID authorId;

  @NotNull(message = "createdAt cannot be null", groups = AdvancedInfo.class)
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @NotNull(message = "updatedAt cannot be null", groups = AdvancedInfo.class)
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @NotNull(message = "gameId cannot be null", groups = InfoUserShouldPass.class)
  @Column(name = "game_id")
  @JoinColumn(name="game_object_id")
  private UUID gameId;

  @JsonIgnoreProperties(value = "gameObjects")
  @ManyToMany(mappedBy = "gameObjects", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  Set<Post> posts;

  public enum Status {
    AVAILABLE,
    SOLD
  }
}
