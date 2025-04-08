package com.example.tea_leaves_project.Responsitory;
import com.example.tea_leaves_project.Model.entity.Package;
import com.example.tea_leaves_project.Model.entity.Users;
import com.example.tea_leaves_project.Model.entity.Users;
import com.example.tea_leaves_project.Model.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
    List<Package> findByUserOrderByPackageidDesc(Users user);
    List<Package> findByWarehouse(Warehouse warehouse);
    Package findByPackageid(long id);
    List<Package> findByStatusAndWarehouse(String status,Warehouse warehouse);
}
