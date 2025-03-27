package com.example.AddressBook.services;

import com.example.AddressBook.Utils.Jwt;
import com.example.AddressBook.dto.LoginDTO;
import com.example.AddressBook.dto.UserDTO;
import com.example.AddressBook.model.Users;
import com.example.AddressBook.repository.UserRepository;
import com.example.AddressBook.serviceInterfaces.UserInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServicesTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Jwt jwtUtil;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServices userServices;

    private Users mockUser;
    private UserDTO mockUserDTO;
    private LoginDTO mockLoginDTO;

    @Mock
    private MessagePublisher messagePublisher;

    @BeforeEach
    void setUp() {
        mockUserDTO = new UserDTO();
        mockUserDTO.setFirstName("Bhupesh");
        mockUserDTO.setLastName("Nauhwar");
        mockUserDTO.setEmail("bhupeshkumar052000@gmail.com");
        mockUserDTO.setPassword("password123");

        mockUser = new Users();
        mockUser.setFirstName("Bhupesh");
        mockUser.setLastName("Nauhwar");
        mockUser.setEmail("bhupeshkumar052000@gmail.com");
        mockUser.setPassword("hashedPassword");
        mockUser.setVerificationToken(UUID.randomUUID().toString());

        mockLoginDTO = new LoginDTO("bhupeshkumar052000@gmail.com", "password123");
    }

    @Test
    void registerUserSuccess() {
        when(userRepository.findByEmail(mockUserDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(mockUserDTO.getPassword())).thenReturn("hashedPpassword");
        when(userRepository.save(any(Users.class))).thenReturn(mockUser);

        String result = userServices.registerUser(mockUserDTO);

        assertEquals("Verification email sent", result);
        verify(emailService).sendVerificationEmail(mockUserDTO.getEmail(), mockUser.getVerificationToken());
        verify(messagePublisher).sendMessage(anyString(), anyString());
    }

    @Test
    void registerUserFailureEmailExists() {
        when(userRepository.findByEmail(mockUserDTO.getEmail())).thenReturn(Optional.of(mockUser));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userServices.registerUser(mockUserDTO);
        });

        assertEquals("Email already exists: " + mockUserDTO.getEmail(), thrown.getMessage());
    }

    @Test
    void registerUserThrowsException() {
        when(userRepository.findByEmail(mockUserDTO.getEmail())).thenThrow(new RuntimeException("Database Error"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userServices.registerUser(mockUserDTO);
        });

        assertEquals("An error occurred while registering the user: Database Error", thrown.getMessage());
    }
    @Test
    void verifyUserSuccess() {
        when(userRepository.findByVerificationToken(mockUser.getVerificationToken())).thenReturn(Optional.of(mockUser));

        String result = userServices.verifyUser(mockUser.getVerificationToken());

        assertEquals("Account verified", result);
        assertNull(mockUser.getVerificationToken());
        assertTrue(mockUser.isVerified());
    }

    @Test
    void verifyUserFailureInvalidToken() {
        when(userRepository.findByVerificationToken("invalid_token")).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userServices.verifyUser("invalid_token");
        });

        assertEquals("Invalid token", thrown.getMessage());
    }

    @Test
    void verifyUserThrowsException() {
        when(userRepository.findByVerificationToken(mockUser.getVerificationToken()))
                .thenThrow(new RuntimeException("Database Error"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userServices.verifyUser(mockUser.getVerificationToken());
        });

        assertEquals("An error occurred during verification: Database Error", thrown.getMessage());
    }

    @Test
    void loginUserSuccess() {
        when(userRepository.findByEmail(mockLoginDTO.getEmail())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(mockLoginDTO.getPassword(), mockUser.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(mockLoginDTO.getEmail())).thenReturn("mock_token");
        when(jwtUtil.validateToken("mock_token")).thenReturn(true);

        String result = userServices.loginUser(mockLoginDTO);

        assertTrue(result.startsWith("Login Successful"));
    }

    @Test
    void loginUserFailureInvalidPassword() {
        when(userRepository.findByEmail(mockLoginDTO.getEmail())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(mockLoginDTO.getPassword(), mockUser.getPassword())).thenReturn(false);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userServices.loginUser(mockLoginDTO);
        });

        assertEquals("Invalid password", thrown.getMessage());
    }

    @Test
    void loginUserThrowsException() {
        when(userRepository.findByEmail(mockLoginDTO.getEmail())).thenThrow(new RuntimeException("Database Error"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userServices.loginUser(mockLoginDTO);
        });

        assertEquals("An error occurred during login: Database Error", thrown.getMessage());
    }
    @Test
    void forgetPasswordSuccess() {
        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(Optional.of(mockUser));
        when(jwtUtil.generateToken(mockUser.getEmail())).thenReturn("reset_token");

        String result = userServices.forgetPassword(mockUser.getEmail());

        assertEquals("Link sent to email.", result);
        verify(emailService).sendResetEmail(mockUser.getEmail(), "reset_token");
    }

    @Test
    void forgetPasswordFailureEmailNotFound() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userServices.forgetPassword("unknown@example.com");
        });

        assertEquals("Email not found", thrown.getMessage());
    }

    @Test
    void forgetPasswordThrowsException() {
        when(userRepository.findByEmail(mockUser.getEmail())).thenThrow(new RuntimeException("Database Error"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userServices.forgetPassword(mockUser.getEmail());
        });

        assertEquals("An error occurred during password reset: Database Error", thrown.getMessage());
    }

    @Test
    void resetPasswordSuccess() {
        when(userRepository.findByResetToken("reset_token")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.encode("new_password")).thenReturn("hashed_new_password");

        String result = userServices.resetPassword("reset_token", "new_password");

        assertEquals("Password reset successful", result);
        assertNull(mockUser.getResetToken());
    }

    @Test
    void resetPasswordFailureInvalidToken() {
        when(userRepository.findByResetToken("invalid_token")).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userServices.resetPassword("invalid_token", "new_password");
        });

        assertEquals("Invalid or expired token", thrown.getMessage());
    }

    @Test
    void resetPasswordThrowsException() {
        when(userRepository.findByResetToken("reset_token")).thenThrow(new RuntimeException("Database Error"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userServices.resetPassword("reset_token", "new_password");
        });

        assertEquals("An error occurred during password reset: Database Error", thrown.getMessage());
    }


}