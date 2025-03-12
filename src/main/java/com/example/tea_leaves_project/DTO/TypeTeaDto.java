package com.example.tea_leaves_project.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TypeTeaDto {
    private long typeteaid;
    private String teaname;
    private String teacode;
}
