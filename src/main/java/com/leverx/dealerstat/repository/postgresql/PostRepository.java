package com.leverx.dealerstat.repository.postgresql;

import com.leverx.dealerstat.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
  List<Post> findByApproved(Boolean approved);
}