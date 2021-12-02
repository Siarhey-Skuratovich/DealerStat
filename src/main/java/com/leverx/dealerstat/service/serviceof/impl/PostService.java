package com.leverx.dealerstat.service.serviceof.impl;

import com.leverx.dealerstat.model.Post;
import com.leverx.dealerstat.repository.postgresql.PostRepository;
import com.leverx.dealerstat.service.serviceof.ServiceOf;
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
  public void create(Post post) {
    postRepository.save(post);
  }

  @Override
  public List<Post> readAll() {
    return postRepository.findAll();
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
