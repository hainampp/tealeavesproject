package com.example.tea_leaves_project.Payload.Request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter

public class PackageRequest {
    private long warehouseid;
    private Date createdtime;
    private long typeteaid;
}
