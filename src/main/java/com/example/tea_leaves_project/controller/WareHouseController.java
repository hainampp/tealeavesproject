package com.example.tea_leaves_project.Controller;

import com.example.tea_leaves_project.DTO.WarehouseDto;
import com.example.tea_leaves_project.Exception.ApiException;
import com.example.tea_leaves_project.Model.entity.Warehouse;
import com.example.tea_leaves_project.Payload.Request.QRScannerData;
import com.example.tea_leaves_project.Payload.Request.WeighRequest;
import com.example.tea_leaves_project.Payload.Response.QrResponse;
import com.example.tea_leaves_project.Service.UserService;
import com.example.tea_leaves_project.Service.WarehouseService;
import com.example.tea_leaves_project.Service.helper.QRServiceHelper;
import com.example.tea_leaves_project.Util.JwtUtilHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/warehouse")
public class WareHouseController {
    @Autowired
    JwtUtilHelper jwtUtil;
    @Autowired
    UserService userService;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    QRServiceHelper qrServiceHelper;
    private String getTokenFromHeader(WebRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    @GetMapping("/allwarehouse")
    public ResponseEntity<?> getAllWarehouse(WebRequest request){
        String token=getTokenFromHeader(request);
        if(token==null){
            throw ApiException.ErrForbidden().build();
        }
        if(!jwtUtil.verifyToken(token)){
            throw ApiException.ErrForbidden().build();
        }
        return new ResponseEntity<>(warehouseService.getAllWarehouse(), HttpStatus.OK);
    }
    @GetMapping("/allpackage/{warehouseid}")
    public ResponseEntity<?> getallPackageByWarehouse(WebRequest request, @PathVariable long warehouseid) {
        String token=getTokenFromHeader(request);
        if(token==null){
            throw ApiException.ErrForbidden().build();
        }
        if(!jwtUtil.verifyToken(token)){
            throw ApiException.ErrForbidden().build();
        }
        return new ResponseEntity<>(warehouseService.getPackageByWarehouse(warehouseid), HttpStatus.OK);
    }
    @PostMapping("/weigh")
    public  ResponseEntity<?> weighPackage(@RequestBody WeighRequest weighRequest) {
        weighRequest.setWeight(weighRequest.getWeight()/1000);
        return new ResponseEntity<>(warehouseService.Weigh(weighRequest), HttpStatus.OK);
    }
    @PutMapping("/scan")
    public  ResponseEntity<?> scanPackage(@RequestParam String scancode,@RequestBody QRScannerData data) {
        return new ResponseEntity<>(warehouseService.scanQrCode(scancode,data), HttpStatus.OK);
    }

}
