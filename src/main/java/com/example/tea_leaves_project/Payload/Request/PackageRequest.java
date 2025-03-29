package com.example.tea_leaves_project.Payload.Request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class PackageRequest {
    private long warehouseid;
    private long typeteaid;
}
