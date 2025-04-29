package com.shivacherukucodes.snapbin.controller;

import com.shivacherukucodes.snapbin.Dto.UserDTO;
import com.shivacherukucodes.snapbin.model.User;
import com.shivacherukucodes.snapbin.security.JwtUtil;
import com.shivacherukucodes.snapbin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Register User
    @PostMapping("/register")
    public String register(@RequestBody UserDTO userDTO) {
        User user = userService.registerUser(userDTO.getUsername(), userDTO.getPassword(), userDTO.getRole());
        return "User registered successfully";
    }

    // User Login (Returns JWT Token)
    @PostMapping("/login")
    public String login(@RequestBody UserDTO userDTO) {
        User user = userService.findUserByUsername(userDTO.getUsername());
        if (user != null && passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            return jwtUtil.generateToken(user.getUsername());
        } else {
            throw new RuntimeException("Invalid Credentials");
        }
    }
}
