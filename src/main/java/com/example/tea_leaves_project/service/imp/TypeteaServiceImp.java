package com.example.tea_leaves_project.service.imp;

import com.example.tea_leaves_project.dto.TypeTeaDto;
import com.example.tea_leaves_project.exception.ApiException;
import com.example.tea_leaves_project.entity.TypeTea;
import com.example.tea_leaves_project.repository.TypeTeaRespository;
import com.example.tea_leaves_project.service.TypeteaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class TypeteaServiceImp implements TypeteaService {
    @Autowired
    TypeTeaRespository typeTeaRespository;
    @Override
    public List<TypeTeaDto> getALlTypeTea() {
        List<TypeTea> typeTeaList = typeTeaRespository.findAll();
        List<TypeTeaDto> typeTeaDtoList = new ArrayList<>();
        for (TypeTea typeTea : typeTeaList) {
            TypeTeaDto typeTeaDto=TypeTeaDto.builder()
                    .teaname(typeTea.getTeaname())
                    .typeteaid(typeTea.getTypeteaid())
                    .teacode(typeTea.getTeacode())
                    .build();
            typeTeaDtoList.add(typeTeaDto);
        }
        return typeTeaDtoList;
    }

    @Override
    public boolean deleteTypeTea(long typeid) {
        TypeTea typeTea=typeTeaRespository.findByTypeteaid(typeid);
        if(typeTea==null){
            throw ApiException.ErrDataLoss().build();
        }
        typeTeaRespository.delete(typeTea);
        return true;
    }

    @Override
    public boolean addTypeTea(TypeTeaDto typeTeaDto) {

        if(typeTeaRespository.existsByTeaname(typeTeaDto.getTeaname())){
            ApiException.ErrExisted().build();
        }
        TypeTea typeTea=new TypeTea();
        typeTea.setTeaname(typeTeaDto.getTeaname());
        typeTea.setTeacode(typeTeaDto.getTeacode());
        typeTeaRespository.save(typeTea);
        return true;
    }
}
