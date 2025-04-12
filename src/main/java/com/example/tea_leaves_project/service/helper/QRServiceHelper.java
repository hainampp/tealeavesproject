package com.example.tea_leaves_project.Service.helper;

import com.example.tea_leaves_project.DTO.QRPackage;
import com.example.tea_leaves_project.Exception.ApiException;
import com.example.tea_leaves_project.Exception.CodeResponse;
import com.example.tea_leaves_project.Model.entity.Package;
import com.example.tea_leaves_project.Model.entity.TypeTea;
import com.example.tea_leaves_project.Model.entity.Users;
import com.example.tea_leaves_project.Model.entity.Warehouse;
import com.example.tea_leaves_project.Payload.Request.PackageRequest;
import com.example.tea_leaves_project.Payload.Response.QrResponse;
import com.example.tea_leaves_project.Responsitory.PackageRepository;
import com.example.tea_leaves_project.Responsitory.TypeTeaRespository;
import com.example.tea_leaves_project.Responsitory.UserRepository;
import com.example.tea_leaves_project.Responsitory.WarehouseRepository;
import com.example.tea_leaves_project.Service.imp.WarehouseServiceImp;
import com.example.tea_leaves_project.constant.QRTag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class QRServiceHelper {

    private final UserRepository userRepository;
    private final WarehouseRepository warehouseRepository;
    private final TypeTeaRespository teaRepository;
    private final PackageRepository packageRepository;

    public String pack(String email, PackageRequest request){
        log.info("Generating QR Code with request: {}, email: {}", request, email);
        Users user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw ApiException.ErrBadCredentials().build();
        }
        Warehouse warehouse = warehouseRepository.findByWarehouseid(request.getWarehouseid());
        TypeTea typeTea = teaRepository.findByTypeteaid(request.getTypeteaid());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Package p = Package.builder()
                .user(user)
                .warehouse(warehouse)
                .createdtime(timestamp)
                .typetea(typeTea)
                .util("Kg")
                .status("Chưa cân")
                .build();
        Package pack = packageRepository.save(p);

        StringBuilder qrcode = new StringBuilder();
        String packageId = String.valueOf(p.getPackageid());
        if (StringUtils.isNotEmpty(packageId)){
            qrcode.append(QRTag.PACKAGE_ID)
                    .append(padLeft(packageId.length()))
                    .append(packageId);
        }

        String userId = String.valueOf(user.getUserid());
        if (StringUtils.isNotEmpty(userId)){
            qrcode.append(QRTag.USER_ID)
                    .append(padLeft(userId.length()))
                    .append(userId);
        }

        String warehouseId = String.valueOf(warehouse.getWarehouseid());
        if (StringUtils.isNotEmpty(warehouseId)){
            qrcode.append(QRTag.WAREHOUSE_ID)
                    .append(padLeft(warehouseId.length()))
                    .append(warehouseId);
        }

        String teaCode = typeTea.getTeacode();
        if (StringUtils.isNotEmpty(teaCode)){
            qrcode.append(QRTag.TEA_CODE)
                    .append(padLeft(teaCode.length()))
                    .append(teaCode);
        }

        long time = System.currentTimeMillis();
        String timestampString = String.valueOf(time);
        if (StringUtils.isNotEmpty(timestampString)){
            qrcode.append(QRTag.CREATED_TIME)
                    .append(padLeft(timestampString.length()))
                    .append(timestampString);
        }
        p.setQrcode(qrcode.toString());
        packageRepository.save(p);
        return p.getQrcode();
    }

    // call: unpack(qrCode, new QrResponse())
    public QrResponse unpack(String qrCode, QrResponse qrResponse){
        log.info("Unpacking QR Code: {}", qrCode);

        // bao gồm header là 2 số định danh QRTag + 2 số chứa độ dài của giá trị
        if(qrCode.length() > 4){
            String header = qrCode.substring(0, 4);
            String tag = header.substring(0, 2);
            String size = header.substring(2);
            String body = qrCode.substring(4, 4 + Integer.parseInt(size));

            switch (tag){
                case QRTag.PACKAGE_ID:
                    qrResponse.setPackageid(Long.parseLong(body));
                    break;
                case QRTag.USER_ID:
                    qrResponse.setUserid(Long.parseLong(body));
                    break;
                case QRTag.WAREHOUSE_ID:
                    qrResponse.setWarehouseid(Long.parseLong(body));
                    break;
                case QRTag.TEA_CODE:
                    qrResponse.setTeacode(body);
                    break;
                case QRTag.CREATED_TIME:
                    try {
                        long time=Long.parseLong(body);
                        System.out.println(time);
                        Timestamp timestamp = new Timestamp(time);
                        qrResponse.setCreatetime(timestamp);
                        System.out.println(timestamp);
                    } catch (Exception ex) {
                        log.error("Parse created date exception: ", ex);
                    }
                    break;
                default:
                    break;
            }
            String subData = qrCode.substring(body.length() + 4);
            unpack(subData, qrResponse);
        }
        return qrResponse;
    }

    private String padLeft(int length){
        if (length > 99) {
            throw ApiException.ErrInvalidArgument()
                    .code(CodeResponse.INVALID_LENGTH)
                    .build();
        }
        return StringUtils.leftPad(String.valueOf(length), 2, "0");
    }
}
