package com.example.tea_leaves_project.Payload.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrResponse {
    long packageid;
    long userid;
    long warehouseid;
    Timestamp createtime;
    String teacode;
    String message;
}
