package com.github.invizible.catdogtion.repository;

import com.github.invizible.catdogtion.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource()
interface UserRepository extends PagingAndSortingRepository<User, Long> {

  @Query("select u from User u where u.username = ?#{principal.username}")
  User findCurrentUser();
}
