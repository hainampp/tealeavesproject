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
    private double total_capacity;
    @Column(name="current_capacity")
    private double current_capacity;
    @Column(name="bincode")
    private String bincode;
    @Column(name="scancode")
    private String scancode;

    @OneToMany(mappedBy = "warehouse")
    List<Package> packages;
}
