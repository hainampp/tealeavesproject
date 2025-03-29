package com.example.tea_leaves_project.Responsitory;

import com.example.tea_leaves_project.Model.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {
    Roles findByRoleid(long id);
}
