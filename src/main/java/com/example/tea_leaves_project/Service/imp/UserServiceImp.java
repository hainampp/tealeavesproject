package com.example.tea_leaves_project.Service.imp;

import com.example.tea_leaves_project.DTO.PackageDto;
import com.example.tea_leaves_project.DTO.UserInfoDto;
import com.example.tea_leaves_project.Exception.ApiException;
import com.example.tea_leaves_project.Model.entity.*;
import com.example.tea_leaves_project.Model.entity.Package;
import com.example.tea_leaves_project.Model.entity.Users;
import com.example.tea_leaves_project.Payload.Request.PackageRequest;
import com.example.tea_leaves_project.Responsitory.PackageRepository;
import com.example.tea_leaves_project.Responsitory.TypeTeaRespository;
import com.example.tea_leaves_project.Responsitory.UserRepository;
import com.example.tea_leaves_project.Responsitory.WarehouseRepository;
import com.example.tea_leaves_project.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
    public String calculateChar(String a){
        int x=a.length();
        if(x<10)return "0"+String.valueOf(x);
        else return ""+String.valueOf(x);
    }
    @Override
    public UserInfoDto getCurrentUserIfo(String email) {
        Users user=userRepository.findUserByEmail(email);
        if(user==null){
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
        Users user=userRepository.findUserByEmail(email);
        if(user==null){
            throw ApiException.ErrBadCredentials().build();
        }
        List<Package> packageList=packageRepository.findByUser(user);
        List<PackageDto> packageDtoList= new ArrayList<>();

        for(Package p : packageList){

            PackageDto packageDto=PackageDto.builder()
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
    public String createPackage(String username, PackageRequest packageRequest) {
        Users user=userRepository.findUserByEmail(username);
        if(user==null){
            throw ApiException.ErrBadCredentials().build();
        }
        Warehouse warehouse=warehouseRepository.findByWarehouseid(packageRequest.getWarehouseid());
        TypeTea typeTea=teaRespository.findByTypeteaid(packageRequest.getTypeteaid());
        Package p= new Package();
        p.setUser(user);
        p.setWarehouse(warehouse);
        p.setCreatedtime(packageRequest.getCreatedtime());
        p.setTypetea(typeTea);
        p.setUtil("Kg");
        p.setStatus("Weighn't yet");
        Package pack=packageRepository.save(p);
        // Gen QR code

        StringBuilder qrcode=new StringBuilder();

        qrcode.append(calculateChar(String.valueOf(p.getPackageid())));
        qrcode.append(String.valueOf(p.getPackageid()));

        qrcode.append(calculateChar(String.valueOf(user.getUserid())));
        qrcode.append(user.getUserid());

        qrcode.append(calculateChar(String.valueOf(warehouse.getWarehouseid())));
        qrcode.append(warehouse.getWarehouseid());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String formattedDateTime = formatter.format(packageRequest.getCreatedtime());
        qrcode.append(calculateChar(formattedDateTime));
        qrcode.append(formattedDateTime);

        qrcode.append(calculateChar(typeTea.getTeacode()));
        qrcode.append(typeTea.getTeacode());

        String qrcodeString=qrcode.toString();

        p.setQrcode(qrcodeString);
        packageRepository.save(p);
        return qrcodeString;
    }
}
