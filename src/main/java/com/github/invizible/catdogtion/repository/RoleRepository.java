package com.github.invizible.catdogtion.repository;

import com.github.invizible.catdogtion.domain.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface RoleRepository extends CrudRepository<Role, Long> {

  Optional<Role> findRoleByName(String name);
}
