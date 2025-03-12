package com.example.tea_leaves_project.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WarehouseDto {
    private long warehouseid;
    private String name;
    private String address;
    private double lat;
    private double lon;
    private long totalpackage;
    private double currentcapacity;
    private  double totalcapacity;
}
