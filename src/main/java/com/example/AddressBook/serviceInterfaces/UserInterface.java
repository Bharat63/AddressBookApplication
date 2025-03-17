package com.example.AddressBook.serviceInterfaces;

import com.example.AddressBook.dto.LoginDTO;
import com.example.AddressBook.dto.UserDTO;

public interface UserInterface {
    public String registerUser(UserDTO userDTO);
    public String verifyUser(String token);

    public String loginUser(LoginDTO loginDTO);

    public String forgetPassword(String email);

    public String resetPassword(String resetToken, String newPassword);

}

