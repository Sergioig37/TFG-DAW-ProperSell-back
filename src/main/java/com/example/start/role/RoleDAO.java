package com.example.start.role;

import org.springframework.data.repository.CrudRepository;

public interface RoleDAO extends CrudRepository<Role, Long> {

	Role findByName(String name);
}
