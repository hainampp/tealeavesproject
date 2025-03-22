package com.example.tea_leaves_project.Service;

import com.example.tea_leaves_project.DTO.TypeTeaDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TypeteaService {
    List<TypeTeaDto> getALlTypeTea();
    boolean deleteTypeTea(long typeid);
    boolean addTypeTea(TypeTeaDto typeTeaDto);
}
