package com.example.AddressBook.services;

import com.example.AddressBook.Utils.Jwt;
import com.example.AddressBook.dto.LoginDTO;
import com.example.AddressBook.dto.UserDTO;
import com.example.AddressBook.model.Users;
import com.example.AddressBook.repository.UserRepository;
import com.example.AddressBook.serviceInterfaces.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.util.StringUtils;

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

    @Autowired
    private MessagePublisher messagePublisher;

    @Autowired
    public UserServices(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder, Jwt jwtUtil, MessagePublisher messagePublisher) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.messagePublisher = messagePublisher;
    }
    public String registerUser(UserDTO userDTO) {
        try {
            if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
                throw new RuntimeException("Email already exists: " + userDTO.getEmail());
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

            messagePublisher.sendMessage("user.registration.queue", "New User Registered: " + user.getEmail());

            return "Verification email sent";
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while registering the user: " + e.getMessage());
        }
    }

    public String verifyUser(String token) {
        try {
            Users user = userRepository.findByVerificationToken(token)
                    .orElseThrow(() -> new RuntimeException("Invalid token"));

            user.setVerificationToken(null);
            user.setVerified(true);
            userRepository.save(user);

            return "Account verified";
        } catch (Exception e) {
            throw new RuntimeException("An error occurred during verification: " + e.getMessage());
        }
    }

    public String loginUser(LoginDTO loginDTO) {
        try {
            Users user = userRepository.findByEmail(loginDTO.getEmail())
                    .orElseThrow(() -> new RuntimeException("Email not found"));

            if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
                throw new RuntimeException("Invalid password");
            }

            if (user.getVerificationToken() != null) {
                throw new RuntimeException("Verify your account");
            }

            String token = jwtUtil.generateToken(user.getEmail());

            if (!jwtUtil.validateToken(token)) {
                throw new RuntimeException("Token validation failed");
            }

            return "Login Successful. Token: " + token;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred during login: " + e.getMessage());
        }
    }

    public String forgetPassword(String email) {
        try {
            if (!StringUtils.hasText(email)) {
                throw new RuntimeException("Email is required");
            }

            Optional<Users> userOptional = userRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                throw new RuntimeException("Email not found");
            }

            Users foundUser = userOptional.get();

            String resetToken = jwtUtil.generateToken(email);
            foundUser.setResetToken(resetToken);
            userRepository.save(foundUser);
            emailService.sendResetEmail(email, resetToken);

            return "Link sent to email.";
        } catch (Exception e) {
            throw new RuntimeException("An error occurred during password reset: " + e.getMessage());
        }
    }

    public String resetPassword(String resetToken, String newPassword) {
        try {
            Optional<Users> userOptional = userRepository.findByResetToken(resetToken);

            if (userOptional.isEmpty()) {
                throw new RuntimeException("Invalid or expired token");
            }

            Users user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null);

            userRepository.save(user);

            return "Password reset successful";
        } catch (Exception e) {
            throw new RuntimeException("An error occurred during password reset: " + e.getMessage());
        }
    }
}
