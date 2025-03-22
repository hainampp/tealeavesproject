package com.example.tea_leaves_project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity(name="typetea")
public class TypeTea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long typeteaid;
    @Column(name="teaname")
    private String teaname;
    @Column(name="teacode")
    private String teacode;

    @OneToMany(mappedBy = "typetea")
    private List<Package> packages;

}
