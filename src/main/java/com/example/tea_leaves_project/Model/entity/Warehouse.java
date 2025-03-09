package com.example.tea_leaves_project.Model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Entity
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long warehouseid;
    @Column(name="name")
    private String name;
    @Column(name="address")
    private String address;
    @Column(name="lat")
    private double lat;
    @Column(name="lon")
    private double lon;
    @Column(name="total_capacity")
    private long total_capacity;
    @Column(name="current_capacity")
    private long current_capacity;

    @OneToMany(mappedBy = "warehouse")
    List<Package> packages;
}
