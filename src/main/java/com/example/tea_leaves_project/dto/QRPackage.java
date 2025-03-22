package com.example.tea_leaves_project.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class QRPackage {

    private PackageDto packageDto;
    private String qrData;
}
