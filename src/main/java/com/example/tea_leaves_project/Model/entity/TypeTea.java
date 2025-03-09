package com.example.tea_leaves_project.Model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

}
