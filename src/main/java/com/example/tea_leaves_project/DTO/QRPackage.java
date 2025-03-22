package com.example.tea_leaves_project.DTO;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class QRPackage {
    private PackageDto packageDto;
    private String qrData;
}
