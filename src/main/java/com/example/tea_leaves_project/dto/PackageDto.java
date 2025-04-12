package com.example.tea_leaves_project.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;
@Getter
@Setter
@Builder
public class PackageDto {
    private long packageId;
    private String fullname;
    private String warehouse;
    private Timestamp createdtime;
    private Timestamp weightime;
    private String typeteaname;
    private double capacity;
    private double humidity;
    private double temperature;
    private String unit;
    private String status;
    private String teacode;
    private String qrcode;
}
