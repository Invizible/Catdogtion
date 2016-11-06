package com.github.invizible.catdogtion.repository;

import com.github.invizible.catdogtion.domain.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {

  Optional<Role> findRoleByName(String name);
}
