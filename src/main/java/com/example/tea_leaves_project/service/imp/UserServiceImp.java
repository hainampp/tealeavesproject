package com.example.tea_leaves_project.service.imp;

import com.example.tea_leaves_project.dto.PackageDto;
import com.example.tea_leaves_project.dto.UserInfoDto;
import com.example.tea_leaves_project.exception.ApiException;
import com.example.tea_leaves_project.entity.Package;
import com.example.tea_leaves_project.entity.Users;
import com.example.tea_leaves_project.Payload.ResponseData;
import com.example.tea_leaves_project.repository.PackageRepository;
import com.example.tea_leaves_project.repository.TypeTeaRespository;
import com.example.tea_leaves_project.repository.UserRepository;
import com.example.tea_leaves_project.repository.WarehouseRepository;
import com.example.tea_leaves_project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImp implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PackageRepository packageRepository;
    @Autowired
    WarehouseRepository warehouseRepository;
    @Autowired
    TypeTeaRespository teaRespository;

    public String calculateChar(String a) {
        int x = a.length();
        if (x < 10) return "0" + x;
        else return String.valueOf(x);
    }

    @Override
    public UserInfoDto getCurrentUserIfo(String email) {
        Users user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw ApiException.ErrBadCredentials().build();
        }
        UserInfoDto userInfoDto = UserInfoDto.builder()
                .fullname(user.getFullname())
                .email(user.getEmail())
                .roles(user.getRoles().getRolename())
                .build();
        return userInfoDto;
    }

    @Override
    public List<PackageDto> getAllPackage(String email) {
        Users user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw ApiException.ErrBadCredentials().build();
        }
        List<Package> packageList = packageRepository.findByUser(user);
        List<PackageDto> packageDtoList = new ArrayList<>();

        for (Package p : packageList) {

            PackageDto packageDto = PackageDto.builder()
                    .packageId(p.getPackageid())
                    .fullname(user.getFullname())
                    .warehouse(p.getWarehouse().getName())
                    .createdtime(p.getCreatedtime())
                    .weightime(p.getWeightime())
                    .typeteaname(p.getTypetea().getTeaname())
                    .teacode(p.getTypetea().getTeacode())
                    .capacity(p.getCapacity())
                    .unit(p.getUtil())
                    .status(p.getStatus())
                    .qrcode(p.getQrcode())
                    .build();
            packageDtoList.add(packageDto);
        }
        return packageDtoList;

    }

    @Override
    public ResponseData deletePackage(String email, long packageId) {
        ResponseData responseData = new ResponseData();
        ResponseData.resp();
        Users user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw ApiException.ErrBadCredentials().build();
        }
        Package pack=packageRepository.findByPackageid(packageId);
        if( !pack.getStatus().equals("Wait delivery") ) {
            try {
                packageRepository.deleteById(packageId);
                responseData.setMessage("Xóa thành công");
            } catch (Exception e) {
                responseData.setMessage(e.getMessage());
                log.error("Lỗi xóa package " + e.getMessage());
            }
        }
        else{
            responseData.setMessage("Không thể xóa gói này");
        }
        return responseData;
    }
}
