package com.example.tea_leaves_project.Responsitory;

import com.example.tea_leaves_project.Model.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
    Warehouse findByWarehouseid(long id);
    Warehouse findByBincode(String bincode);
    Warehouse findByScancode(String scancode);
}
