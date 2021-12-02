package com.leverx.dealerstat.repository.postgresql;

import com.leverx.dealerstat.model.GameObject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameObjectRepository extends JpaRepository<GameObject, UUID> {
}
