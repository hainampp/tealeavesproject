package com.example.tea_leaves_project.Model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="package")
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long packageid;

    @ManyToOne
    @JoinColumn(name="userid")
    private Users user;

    @ManyToOne
    @JoinColumn(name="warehouseid")
    private Warehouse warehouse;

    @Column(name="created_time")
    @Temporal(TemporalType.DATE)
    private Date createdtime;

    @Column(name="weigh_time")
    @Temporal(TemporalType.DATE)
    private Date weightime;

    @ManyToOne
    @JoinColumn(name="typeteaid")
    private TypeTea typetea;

    @Column(name="capacity")
    private double capacity;

    @Column(name="util")
    private String util;

    @Column(name="status")
    private String status;

    @Column(name="qrcode")
    private String qrcode;

}
