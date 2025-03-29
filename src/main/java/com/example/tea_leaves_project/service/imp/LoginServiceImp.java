package com.example.tea_leaves_project.Service.imp;

import com.example.tea_leaves_project.Exception.ApiException;
import com.example.tea_leaves_project.Model.entity.Roles;
import com.example.tea_leaves_project.Model.entity.Users;
import com.example.tea_leaves_project.Model.entity.Users;
import com.example.tea_leaves_project.Payload.Request.SignupRequest;
import com.example.tea_leaves_project.Responsitory.RolesRepository;
import com.example.tea_leaves_project.Responsitory.UserRepository;
import com.example.tea_leaves_project.Service.LoginService;
import com.example.tea_leaves_project.Util.JwtUtilHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class LoginServiceImp implements LoginService {
    @Autowired
    JwtUtilHelper jwtUtilHelper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RolesRepository rolesRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public boolean createUser(SignupRequest signupRequest) {
        Users user=userRepository.findUserByEmail(signupRequest.getEmail());
        if(user!=null){
            throw ApiException.ErrExisted().build();
        }
        log.info("[UserServiceImpl - createUser] signupRequest: {}", signupRequest);

        Users newUser=new Users();
        newUser.setEmail(signupRequest.getEmail());
        newUser.setFullname(signupRequest.getFullname());
        newUser.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        Roles roles= rolesRepository.findByRoleid(signupRequest.getRole_id());
        newUser.setRoles(roles);
        userRepository.save(newUser);
        return true;

    }

    @Override
    public String authUser(String email, String password) {
        Users user=userRepository.findUserByEmail(email);
        if(user==null){
            throw ApiException.ErrBadCredentials().build();
        }
        if(!passwordEncoder.matches(password,user.getPassword())){
            throw ApiException.ErrBadCredentials().build();
        }
        List<String> roleList=new ArrayList<>();
        roleList.add(user.getRoles().getRolename());
        return jwtUtilHelper.generateToken(email,roleList);
    }
}
