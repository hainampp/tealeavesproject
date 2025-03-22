package com.example.tea_leaves_project.service;

import com.example.tea_leaves_project.Payload.Request.SignupRequest;

public interface LoginService {
    boolean createUser(SignupRequest signupRequest);
    String authUser(String email, String password);
}
