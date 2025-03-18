package com.example.tea_leaves_project.Payload.Request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
public class PackageRequest {
    private long warehouseid;
    private Date createdtime;
    private long typeteaid;
}
