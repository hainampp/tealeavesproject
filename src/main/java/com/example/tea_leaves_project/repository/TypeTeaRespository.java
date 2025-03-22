package com.example.tea_leaves_project.repository;

import com.example.tea_leaves_project.entity.TypeTea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeTeaRespository extends JpaRepository<TypeTea,Integer> {
    TypeTea findByTypeteaid(long typeteaid);
    boolean existsByTeaname(String teaname);
}
