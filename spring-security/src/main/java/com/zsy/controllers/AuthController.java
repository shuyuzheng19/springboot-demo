package com.zsy.controllers;

import com.zsy.utils.JwtUtils;
import com.zsy.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * @author 郑书宇
 * @create 2022/12/13 1:41
 * @desc
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @GetMapping("/get")
    public String getUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @GetMapping("/admin")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public String admin(){
        return "ADMIN";
    }

}
