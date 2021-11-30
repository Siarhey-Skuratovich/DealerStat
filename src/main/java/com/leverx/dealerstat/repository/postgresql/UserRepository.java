package com.leverx.dealerstat.repository.postgresql;

import com.leverx.dealerstat.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

  @Query("select u from UserEntity u where u.email = ?1")
  UserEntity findByEmail(String email);
}
