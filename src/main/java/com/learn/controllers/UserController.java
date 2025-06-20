package com.learn.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.dto.ResponseData;
import com.learn.dto.UserData;
import com.learn.entities.User;
import com.learn.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    public ResponseEntity<ResponseData<User>> register(@RequestBody UserData userData) {
        ResponseData<User> response = new ResponseData<>();
        User user = modelMapper.map(userData, User.class);
        response.setPayload(userService.registerUser(user));
        response.setStatus(true);
        response.getMessages().add("User saved...");
        return ResponseEntity.ok(response);
    }

}
