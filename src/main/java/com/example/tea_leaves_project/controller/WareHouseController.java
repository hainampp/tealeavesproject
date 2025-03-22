package com.example.tea_leaves_project.controller;

import com.example.tea_leaves_project.exception.ApiException;
import com.example.tea_leaves_project.Payload.Request.WeighRequest;
import com.example.tea_leaves_project.Payload.Response.QrResponse;
import com.example.tea_leaves_project.service.UserService;
import com.example.tea_leaves_project.service.WarehouseService;
import com.example.tea_leaves_project.service.helper.QRServiceHelper;
import com.example.tea_leaves_project.util.JwtUtilHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

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
        return new ResponseEntity<>(warehouseService.Weigh(weighRequest), HttpStatus.OK);
    }
    @PutMapping("/scan")
    public  ResponseEntity<?> scanPackage(@RequestParam String qrcode) {
        QrResponse qrResponse =new QrResponse();
        return new ResponseEntity<>(qrServiceHelper.unpack(qrcode,qrResponse), HttpStatus.OK);
    }

}
