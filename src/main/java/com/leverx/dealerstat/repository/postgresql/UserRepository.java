package com.leverx.dealerstat.repository.postgresql;

import com.leverx.dealerstat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
