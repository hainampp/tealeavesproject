package com.example.tea_leaves_project.repository;
import com.example.tea_leaves_project.entity.Package;
import com.example.tea_leaves_project.entity.Users;
import com.example.tea_leaves_project.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
    List<Package> findByUser(Users user);
    List<Package> findByWarehouse(Warehouse warehouse);
    Package findByPackageid(long id);
    List<Package> findByStatusAndWarehouse(String status,Warehouse warehouse);
}
