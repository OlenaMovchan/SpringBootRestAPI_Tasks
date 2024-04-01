package com.example.tasknewspring.controller;

import com.example.tasknewspring.aspect.LogExecutionTime;
import com.example.tasknewspring.dto.UserDto;
import com.example.tasknewspring.service.UserInfoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@AllArgsConstructor
@SecurityRequirement(name = "basicAuth")
@RequestMapping()
public class UserController {

    private UserInfoService service;

    @PostMapping("/addNewUser")
    @LogExecutionTime
    public UserDto addNewUser(@RequestBody UserDto userInfo) {
        return service.addUser(userInfo);
    }

}
