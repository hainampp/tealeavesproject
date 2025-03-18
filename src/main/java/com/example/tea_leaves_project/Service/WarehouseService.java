package com.example.tea_leaves_project.Service;

import com.example.tea_leaves_project.DTO.WarehouseDto;
import com.example.tea_leaves_project.DTO.WarehousePackageDto;
import com.example.tea_leaves_project.Payload.Request.WeighRequest;
import com.example.tea_leaves_project.Payload.Response.QrResponse;
import com.example.tea_leaves_project.Payload.ResponseData;

import java.util.List;

public interface WarehouseService {
    List<WarehouseDto> getAllWarehouse();
    WarehousePackageDto getPackageByWarehouse(long warehouseid);
    String scanQrCode(long packageid);
    ResponseData Weigh(WeighRequest weighRequest);}
