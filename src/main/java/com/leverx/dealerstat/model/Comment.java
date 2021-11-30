package com.leverx.dealerstat.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "comments")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Comment {
  @Id
  @Column(name = "id")
  private UUID id;

  @Column(name = "message")
  private String message;

  @Column(name = "post_id")
  private UUID postId;

  @Column(name = "author_id")
  private UUID authorId;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "approved")
  private Boolean approved;
}
