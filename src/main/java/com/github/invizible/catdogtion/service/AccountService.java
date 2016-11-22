package com.github.invizible.catdogtion.service;

import com.github.invizible.catdogtion.domain.User;
import com.github.invizible.catdogtion.repository.RoleRepository;
import com.github.invizible.catdogtion.repository.UserRepository;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.github.invizible.catdogtion.Constants.USER_ROLE;

@Service
@Transactional
public class AccountService {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private UserRepository userRepository;

  public User createUser(User user, String password) {
    user.setPassword(passwordEncoder.encode(password));
    user.setRoles(Sets.newHashSet(roleRepository.findRoleByName(USER_ROLE).get()));

    return userRepository.save(user);
  }
}
