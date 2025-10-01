package com.ecom.project.repositories;

import com.ecom.project.model.AppRole;
import com.ecom.project.model.Role;
import com.ecom.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
