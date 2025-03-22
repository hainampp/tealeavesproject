package com.example.tea_leaves_project.Service;

import com.example.tea_leaves_project.DTO.PackageDto;
import com.example.tea_leaves_project.DTO.UserInfoDto;
import com.example.tea_leaves_project.Payload.Request.PackageRequest;
import com.example.tea_leaves_project.Payload.ResponseData;

import java.util.List;

public interface UserService {
    UserInfoDto getCurrentUserIfo(String email);
    List<PackageDto> getAllPackage(String email);
    ResponseData deletePackage(String email, long packageId);
}
