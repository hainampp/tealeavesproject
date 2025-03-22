package com.example.tea_leaves_project.service;

import com.example.tea_leaves_project.dto.UserDto;
import com.example.tea_leaves_project.Payload.ResponseData;

import java.util.List;

public interface AdminService {
    List<UserDto> getAllUsers();
    ResponseData deleteUser(long userId);
}
