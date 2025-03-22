package com.example.tea_leaves_project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Builder
public class WarehousePackageDto {
    private long warehouseid;
    private String name;
    List<PackageDto> packages;
}
