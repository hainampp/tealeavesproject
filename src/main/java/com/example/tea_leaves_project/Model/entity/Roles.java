package com.example.tea_leaves_project.Model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Entity
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roleid;
    @Column(name="rolename")
    private String rolename;
    @OneToMany(mappedBy = "roles")
    List<Users> users;

}
