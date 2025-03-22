package com.example.tea_leaves_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QRScannerData {

    @JsonProperty("temperature")
    private double temperature;

    @JsonProperty("humidity")
    private double humidity;

    @JsonProperty("qr_code")
    private String qrCode;
}
