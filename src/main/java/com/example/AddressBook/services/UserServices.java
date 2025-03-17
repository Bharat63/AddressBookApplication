package com.example.AddressBook.services;

import com.example.AddressBook.Exception.RegistrationException;
import com.example.AddressBook.Exception.VerificationException;
import com.example.AddressBook.Utils.Jwt;
import com.example.AddressBook.dto.LoginDTO;
import com.example.AddressBook.dto.UserDTO;
import com.example.AddressBook.model.Users;
import com.example.AddressBook.repository.UserRepository;
import com.example.AddressBook.serviceInterfaces.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.AddressBook.Exception.AuthenticationException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServices implements UserInterface {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Jwt jwtUtil;
    @Autowired
    EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    public UserServices(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }
    public String registerUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RegistrationException("Email already exists");
        }

        Users user = new Users();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), verificationToken);

        return "Verification email sent";
    }

    public String verifyUser(String token) {
        Users user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new VerificationException("Invalid token"));

        user.setVerificationToken(null);
        userRepository.save(user);

        return "Account verified";
    }
    public String loginUser(LoginDTO loginDTO) {
        Users user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new AuthenticationException("Email not found"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Invalid password");
        }

        if (user.getVerificationToken() != null) {
            throw new AuthenticationException("Verify your account");
        }

        String token = jwtUtil.generateToken(user.getEmail());


        if (!jwtUtil.validateToken(token)) {
            System.out.println("Token validation failed!");
            throw new AuthenticationException("Token validation failed");
        }

        return "Login Successful. Token: " + token;
    }

}
