package com.leverx.dealerstat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.leverx.dealerstat.validation.groups.AdvancedInfo;
import com.leverx.dealerstat.validation.groups.InfoUserShouldPass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserEntity {
  @Id
  @Column(name = "user_id")
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
          name = "UUID",
          strategy = "org.hibernate.id.UUIDGenerator"
  )
  private UUID userId;

  @NotBlank(message = "firstName cannot be null, empty, blank", groups = InfoUserShouldPass.class)
  @Column(name = "first_name")
  private String firstName;

  @NotBlank(message = "lastName cannot be null, empty, blank", groups = InfoUserShouldPass.class)
  @Column(name = "last_name")
  private String lastName;

  @NotBlank(message = "password cannot be null, empty, blank", groups = InfoUserShouldPass.class)
  @Column(name = "password")
  private String password;

  @NotBlank(message = "email cannot be null, empty, blank", groups = InfoUserShouldPass.class)
  @Email(message = "Email should be valid")
  @Column(name = "email")
  private String email;

  @NotNull(groups = AdvancedInfo.class)
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @NotNull(message = "role should be either TRADER, or ANONYM", groups = InfoUserShouldPass.class)
  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  private Role role;

  @AssertFalse(groups = InfoUserShouldPass.class)
  @Column(name = "enabled")
  private Boolean enabled;

  @JsonIgnore
  @OneToMany(mappedBy = "traderId", fetch = FetchType.LAZY)
  private Set<Post> posts;

  public enum Role {
    ADMIN,
    TRADER,
    ANONYM
  }
}
