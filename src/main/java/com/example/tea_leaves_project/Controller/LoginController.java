package com.example.tea_leaves_project.Controller;

import com.example.tea_leaves_project.Payload.Request.SigninRequest;
import com.example.tea_leaves_project.Payload.Request.SignupRequest;
import com.example.tea_leaves_project.Payload.ResponseData;
import com.example.tea_leaves_project.Service.LoginService;
import com.example.tea_leaves_project.Service.UserService;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    LoginService loginService;
    @PostMapping("/signup")
    public ResponseEntity<ResponseData> createUser(@RequestBody SignupRequest signupRequest) {
        ResponseData responseData = ResponseData.resp();
        boolean success = loginService.createUser(signupRequest);
        return ResponseEntity.ok(responseData);
    }
    @GetMapping("/signin")
    public ResponseEntity<ResponseData> authUser(@RequestParam String email, @RequestParam String password) {
        ResponseData responseData = ResponseData.resp();
//        String email=signinRequest.getEmail();
//        String password=signinRequest.getPassword();
        String token=loginService.authUser(email,password);
        responseData.setData(token);
        return ResponseEntity.ok(responseData);
    }

}
