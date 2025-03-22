package com.example.tea_leaves_project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.List;
@Getter
@Setter
@Entity(name="user")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userid;
    @Column(name="email")
    private String email;
    @Column(name="fullname")
    private String fullname;
    @Column(name="password")
    private String password;

    @OneToMany(mappedBy = "user")
    List<Package> packages;

    @ManyToOne
    @JoinColumn(name="roleid")
    private Roles roles;
}
