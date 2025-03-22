package com.example.tea_leaves_project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@Builder
public class PackageDto {
    private long packageId;
    private String fullname;
    private String warehouse;
    private Date createdtime;
    private Date weightime;
    private String typeteaname;
    private double capacity;
    private String unit;
    private String status;
    private String teacode;
    private String qrcode;
}
