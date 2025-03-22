package com.example.tea_leaves_project.repository;

import com.example.tea_leaves_project.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findUserByEmail(String email);

}
