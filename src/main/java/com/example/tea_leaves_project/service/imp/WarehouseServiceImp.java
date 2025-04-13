package com.example.tea_leaves_project.Service.imp;

import com.example.tea_leaves_project.DTO.PackageDto;
import com.example.tea_leaves_project.DTO.WarehouseDto;
import com.example.tea_leaves_project.DTO.WarehousePackageDto;
import com.example.tea_leaves_project.Exception.ApiException;
import com.example.tea_leaves_project.Model.entity.Package;
import com.example.tea_leaves_project.Model.entity.Warehouse;
import com.example.tea_leaves_project.Payload.Request.QRScannerData;
import com.example.tea_leaves_project.Payload.Request.WeighRequest;
import com.example.tea_leaves_project.Payload.Response.QrResponse;
import com.example.tea_leaves_project.Payload.ResponseData;
import com.example.tea_leaves_project.Responsitory.PackageRepository;
import com.example.tea_leaves_project.Responsitory.WarehouseRepository;
import com.example.tea_leaves_project.Service.WarehouseService;
import com.example.tea_leaves_project.Service.helper.QRServiceHelper;
import com.example.tea_leaves_project.Service.helper.SendSSEHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class WarehouseServiceImp implements WarehouseService {
    @Autowired
    WarehouseRepository warehouseRepository;
    @Autowired
    PackageRepository packageRepository;
    @Autowired
    QRServiceHelper qrServiceHelper;
    @Autowired
    SendSSEHelper sendSSEHelper;
    // tính số bao chè sẵn sàng vận chuyển
    public long calculateTotalPackage(Warehouse warehouse) {
        long sum = 0;
        for (Package p : warehouse.getPackages()) {
            if (p.getStatus().equals("Wait delivery")) sum++;
        }
        return sum;
    }

    @Override
    public List<WarehouseDto> getAllWarehouse() {
        List<Warehouse> warehouses = warehouseRepository.findAll();
        List<WarehouseDto> warehouseDtos = new ArrayList<>();
        for (Warehouse warehouse : warehouses) {
            WarehouseDto warehouseDto = WarehouseDto.builder()
                    .warehouseid(warehouse.getWarehouseid())
                    .name(warehouse.getName())
                    .address(warehouse.getAddress())
                    .lat(warehouse.getLat())
                    .lon(warehouse.getLon())
                    .totalpackage(calculateTotalPackage(warehouse))
                    .currentcapacity(warehouse.getCurrent_capacity())
                    .totalcapacity(warehouse.getTotal_capacity())
                    .build();
            warehouseDtos.add(warehouseDto);
        }
        return warehouseDtos;
    }

    @Override
    public WarehousePackageDto getPackageByWarehouse(long warehouseid) {

        Warehouse warehouse = warehouseRepository.findByWarehouseid(warehouseid);

        if (warehouse == null) {
            throw ApiException.ErrDataLoss().build();
        }

        List<Package> plist = packageRepository.findByWarehouse(warehouse);
        List<PackageDto> packageDtoList = new ArrayList<>();
        for (Package p : plist) {

            PackageDto packageDto = PackageDto.builder()
                    .packageId(p.getPackageid())
                    .fullname(p.getUser().getFullname())
                    .warehouse(p.getWarehouse().getName())
                    .createdtime(p.getCreatedtime())
                    .weightime(p.getWeightime())
                    .capacity(p.getCapacity())
                    .unit(p.getUtil())
                    .status(p.getStatus())
                    .teacode(p.getTypetea().getTeacode())
                    .humidity(p.getHumidity())
                    .temperature(p.getTemperature())
                    .build();
            packageDtoList.add(packageDto);
        }
        // sắp xếp giảm dần
        Collections.sort(packageDtoList,(p1,p2) ->p1.getPackageId()>p2.getPackageId()? -1 : 1 );

        WarehousePackageDto warehousePackageDto = WarehousePackageDto.builder()
                .warehouseid(warehouseid)
                .name(warehouse.getName())
                .packages(packageDtoList).build();
        return warehousePackageDto;
    }
    @Override
    public QrResponse scanQrCode(String scancode,QRScannerData data) {

        Warehouse warehouse=warehouseRepository.findByScancode(scancode);

        if(warehouse==null){
            log.info("[WarehouseService- scanQrCode] Không tìm thấy Warehouse với {}",scancode);
            throw ApiException.ErrNotFound().build();
        }

        QrResponse qrres= new QrResponse();
        QrResponse qrResponse = qrServiceHelper.unpack(data.getQrCode(),qrres);
        Package p = packageRepository.findByPackageid(qrResponse.getPackageid());

        if (p == null) {
            throw ApiException.ErrNotFound().build();
        }

        if(p.getWarehouse()!=warehouse){
            log.info("[WarehouseService- scanQrCode] Lỗi {} của {} và kho hiện tại( {} ) không trùng nhau ",p.getWarehouse().getName(),p.getPackageid(),warehouse.getName());
            throw ApiException.ErrDataLoss().build();
        }

        if (p.getStatus().equals("Chưa cân")) {
            p.setStatus("Đã quét");
            p.setTemperature(data.getTemperature());
            p.setHumidity(data.getHumidity());
            packageRepository.save(p);
           qrResponse.setMessage("Quét thành công");
        }
        if (p.getStatus().equals("Đã quét") || p.getStatus().equals("Chờ vận chuyển")) {
            qrResponse.setMessage("Sản phẩm đã được quét");
        }

        //send scanned notice
        sendSSEHelper.notifyQrCodeScanned(p.getUser().getUserid(),p.getUser().getFullname());

        return qrResponse;
    }

    @Override
    public ResponseData Weigh(WeighRequest weighRequest) {
        log.info("[WarehouseService- Weigh] {} {}", weighRequest.getWeight(),weighRequest.getBin_code());
        String bin_code = weighRequest.getBin_code();
        ResponseData responseData = new ResponseData();
        ResponseData.resp();
        Warehouse warehouse = warehouseRepository.findByBincode(bin_code);
        if (warehouse == null) {
            log.info("[WarehouseService- Weigh] Không tìm thấy cân");
            throw ApiException.ErrDataLoss().build();
        }
        List<Package> p = packageRepository.findByStatusAndWarehouse("Đã quét", warehouse);
        if (p.size() == 0) {
            responseData.setMessage("Không tìm thấy bao scan gần nhất");
            log.info("[WarehouseService- Weigh] Không tìm thấy bao scan gần nhất");
            return responseData;
        }
        if (p.size() > 1) {
            for (Package ps : p) {
                ps.setStatus("Chưa cân");
                packageRepository.save(ps);
            }
        }
        Package plast = p.getLast();
        plast.setStatus("Chờ vận chuyển");
        plast.setCapacity(weighRequest.getWeight());

        packageRepository.save(plast);
        // cập nhật tổng cân nặng
        warehouse.setCurrent_capacity(warehouse.getCurrent_capacity() + weighRequest.getWeight());
        warehouseRepository.save(warehouse);
        // send notice
        sendSSEHelper.notifyWeigh(plast.getUser().getUserid(),plast.getUser().getFullname(),weighRequest.getWeight());

        responseData.setMessage("Cân thành công");
        return responseData;
    }
}
