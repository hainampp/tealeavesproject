package com.example.tea_leaves_project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserInfoDto {
    String email;
    String fullname;
    String roles;
}
