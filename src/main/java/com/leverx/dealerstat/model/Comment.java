package com.leverx.dealerstat.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "comments")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Comment {
  @Id
  @Column(name = "comment_id")
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
          name = "UUID",
          strategy = "org.hibernate.id.UUIDGenerator"
  )
  private UUID commentId;

  @Column(name = "message")
  private String message;

  @Column(name = "post_id")
  @JoinColumn(name="comment_id", nullable=false)
  private UUID postId;

  @Column(name = "author_id")
  private UUID authorId;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "approved")
  private Boolean approved;
}
