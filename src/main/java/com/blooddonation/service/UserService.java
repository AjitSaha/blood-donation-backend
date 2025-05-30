package com.blooddonation.service;

import com.blooddonation.dto.LoginRequest;
import com.blooddonation.dto.LoginResponse;
import com.blooddonation.dto.UserDTO;
import com.blooddonation.model.User;

public interface UserService {
    
    UserDTO registerUser(UserDTO userDTO);
    
    LoginResponse login(LoginRequest loginRequest);
    
    UserDTO getUserById(Long id);
    
    UserDTO getUserByEmail(String email);
    
    UserDTO updateUser(Long id, UserDTO userDTO);
    
    void deleteUser(Long id);
    
    boolean isUserExists(String email);
    
    // Helper methods for conversion
    UserDTO convertToDTO(User user);
    
    User convertToEntity(UserDTO userDTO);
}