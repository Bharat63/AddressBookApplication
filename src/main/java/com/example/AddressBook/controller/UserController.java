package com.example.AddressBook.controller;

import com.example.AddressBook.dto.LoginDTO;
import com.example.AddressBook.dto.UserDTO;
import com.example.AddressBook.services.UserServices;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserServices userServices;

    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
        logger.info("Received Registration Request: {}", userDTO);

        String response = userServices.registerUser(userDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String token) {
        logger.info("Received verification request for token: {}", token);
        String response = userServices.verifyUser(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        logger.info("Received login request for email: {}", loginDTO.getEmail());

        String token = userServices.loginUser(loginDTO);
        return ResponseEntity.ok("{\"message\": \"Login successful!\", \"token\": \"" + token + "\"}");
    }








}
