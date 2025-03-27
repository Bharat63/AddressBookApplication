package com.example.AddressBook.controller;

import com.example.AddressBook.Utils.Jwt;
import com.example.AddressBook.dto.ApiResponse;
import com.example.AddressBook.dto.LoginDTO;
import com.example.AddressBook.dto.ResetPasswordDTO;
import com.example.AddressBook.dto.UserDTO;
import com.example.AddressBook.services.UserServices;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserServices userServices;

    @Autowired
    Jwt jwtUtil;
    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody UserDTO userDTO) {
        logger.info("Received Registration Request: {}", userDTO);

        ApiResponse response = userServices.registerUser(userDTO);

        if (!response.isSuccess()) {
            if (response.getMessage().contains("Email already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

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


    @PostMapping("/forget-password")
    public ResponseEntity<String> forgetPassword(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userServices.forgetPassword(userDTO.getEmail()));
    }

    @PostMapping("reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token, @RequestBody ResetPasswordDTO resetPasswordDTO){
        return ResponseEntity.ok(userServices.resetPassword(token,resetPasswordDTO.getNewPassword()));
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {

        String authToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String email = jwtUtil.extractEmail(authToken);

        if (email == null) {
            return ResponseEntity.badRequest().body("Invalid token or email not found.");
        }

        logger.info("Logout request received for user: {}", email);

        userServices.logoutUser(email, authToken);
        return ResponseEntity.ok("User logged out successfully.");
    }




}
