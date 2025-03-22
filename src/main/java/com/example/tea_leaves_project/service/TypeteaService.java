package com.example.tea_leaves_project.service;

import com.example.tea_leaves_project.dto.TypeTeaDto;

import java.util.List;

public interface TypeteaService {
    List<TypeTeaDto> getALlTypeTea();
    boolean deleteTypeTea(long typeid);
    boolean addTypeTea(TypeTeaDto typeTeaDto);
}
