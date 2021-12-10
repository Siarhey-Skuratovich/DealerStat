package com.leverx.dealerstat.repository.postgresql;

import com.leverx.dealerstat.model.GameObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameObjectRepository extends JpaRepository<GameObject, UUID> {
}
