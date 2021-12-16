package com.leverx.dealerstat.service.user;

import com.leverx.dealerstat.model.UserEntity;
import com.leverx.dealerstat.repository.postgresql.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;

@Service("userDetailsService")
@Transactional
public class UserDetailServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;

  public UserDetailServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    final UserEntity userEntity = userRepository.findByEmail(email);
    boolean enabled = !userEntity.getEnabled();

    if (userEntity == null || !enabled) {
      throw new UsernameNotFoundException(email);
    }

    return User.withUsername(userEntity.getEmail())
            .password(userEntity.getPassword())
            .disabled(!userEntity.getEnabled())
            .authorities(getAuthorities(userEntity)).build();
  }

  private Collection<GrantedAuthority> getAuthorities(UserEntity user){
    Collection<GrantedAuthority> authorities = new HashSet<>();
      authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
    return authorities;
  }
}
