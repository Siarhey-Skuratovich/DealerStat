package com.leverx.dealerstat.service.serviceof.impl;

import com.leverx.dealerstat.config.WebSecurityConfig;
import com.leverx.dealerstat.model.Post;
import com.leverx.dealerstat.repository.postgresql.PostRepository;
import com.leverx.dealerstat.service.serviceof.ServiceOf;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PostService implements ServiceOf<Post> {
  private final PostRepository postRepository;

  public PostService(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @Override
  public Post create(Post post) {
    return  postRepository.save(post);
  }

  @Override
  public List<Post> readAll() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication.getName().equals(WebSecurityConfig.ADMIN_USERNAME)) {
      return postRepository.findAll();
    } else {
      return postRepository.findByApproved(true);
    }
  }

  @Override
  public Post read(UUID id) {
    return postRepository.getById(id);
  }

  @Override
  public boolean update(Post post) {
    if (postRepository.existsById(post.getPostId())) {
      postRepository.save(post);
      return true;
    }
    return false;
  }

  @Override
  public boolean delete(UUID id) {
    if (postRepository.existsById(id)) {
      postRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
