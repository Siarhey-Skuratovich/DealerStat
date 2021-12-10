package com.leverx.dealerstat.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.leverx.dealerstat.validation.groups.AdvancedInfo;
import com.leverx.dealerstat.validation.groups.InfoUserShouldPass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

  @NotBlank(message = "message cannot be null, empty, blank", groups = InfoUserShouldPass.class)
  @Column(name = "message")
  private String message;

  @NotNull(message = "postId cannot be null", groups = AdvancedInfo.class)
  @Column(name = "post_id")
  @JoinColumn(name="comment_id")
  private UUID postId;

  @NotNull(message = "authorId cannot be null", groups = AdvancedInfo.class)
  @Column(name = "author_id")
  private UUID authorId;

  @NotNull(message = "createdAt cannot be null", groups = AdvancedInfo.class)
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @AssertFalse(groups = InfoUserShouldPass.class)
  @Column(name = "approved")
  private Boolean approved;
}
