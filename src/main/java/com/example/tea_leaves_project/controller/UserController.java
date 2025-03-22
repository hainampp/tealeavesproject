package com.example.tea_leaves_project.controller;

import com.example.tea_leaves_project.exception.ApiException;
import com.example.tea_leaves_project.Payload.Request.PackageRequest;
import com.example.tea_leaves_project.service.UserService;
import com.example.tea_leaves_project.service.helper.QRServiceHelper;
import com.example.tea_leaves_project.util.JwtUtilHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    JwtUtilHelper jwtUtil;
    @Autowired
    UserService userService;
    @Autowired
    QRServiceHelper qrServiceHelper;

    private String getTokenFromHeader(WebRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserIfo(WebRequest request){
        String token=getTokenFromHeader(request);
        if(token==null){
            throw ApiException.ErrForbidden().build();
        }
        if(!jwtUtil.verifyToken(token)){
            throw ApiException.ErrForbidden().build();
        }
        String email= jwtUtil.getEmail(token);
        return new ResponseEntity<>(userService.getCurrentUserIfo(email), HttpStatus.OK);
    }

    @PostMapping("/qrcode")
    public ResponseEntity<?> GenQrCode(WebRequest request, @RequestBody PackageRequest packageRequest){
        String token=getTokenFromHeader(request);
        if(token==null){
            throw ApiException.ErrForbidden().build();
        }
        if(!jwtUtil.verifyToken(token)){
            throw ApiException.ErrForbidden().build();
        }
        String email= jwtUtil.getEmail(token);
        return new ResponseEntity<>(qrServiceHelper.pack(email, packageRequest), HttpStatus.OK);
    }
    @GetMapping("/allpackage")
    public ResponseEntity<?> getAllPackage(WebRequest request){
        String token=getTokenFromHeader(request);
        if(token==null){
            throw ApiException.ErrForbidden().build();
        }
        if(!jwtUtil.verifyToken(token)){
            throw ApiException.ErrForbidden().build();
        }
        String email= jwtUtil.getEmail(token);
        return new ResponseEntity<>(userService.getAllPackage(email), HttpStatus.OK);
    }
    @DeleteMapping("/package/{packageid}")
    public ResponseEntity<?> deletePackage(WebRequest request, @PathVariable long packageid){
        String token=getTokenFromHeader(request);
        if(token==null){
            throw ApiException.ErrForbidden().build();
        }
        if(!jwtUtil.verifyToken(token)){
            throw ApiException.ErrForbidden().build();
        }
        String email= jwtUtil.getEmail(token);
        return new ResponseEntity<>(userService.deletePackage(email, packageid), HttpStatus.OK);
    }
}
