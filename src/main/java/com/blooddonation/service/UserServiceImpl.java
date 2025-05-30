package com.blooddonation.service;

import com.blooddonation.dto.LoginRequest;
import com.blooddonation.dto.LoginResponse;
import com.blooddonation.dto.UserDTO;
import com.blooddonation.exception.ResourceNotFoundException;
import com.blooddonation.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.blooddonation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
	
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        
        User user = convertToEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User savedUser = userRepository.save(user);
        
        // Don't return password in response
        UserDTO responseDTO = convertToDTO(savedUser);
        responseDTO.setPassword(null);
        return responseDTO;
    }
    
 // Update UserServiceImpl.java login method
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        logger.debug("Login attempt for email: {}", loginRequest.getEmail());
        
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", loginRequest.getEmail());
                    return new ResourceNotFoundException("User not found with email: " + loginRequest.getEmail());
                });
        
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            logger.error("Invalid password for user: {}", loginRequest.getEmail());
            throw new IllegalArgumentException("Invalid password");
        }
        
        // Generate a simple token (in a real app, use JWT or OAuth)
        String token = UUID.randomUUID().toString();
        logger.debug("Login successful for user: {}", loginRequest.getEmail());
        
        return new LoginResponse(user.getId(), user.getName(), user.getEmail(), token);
    }
    
    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        UserDTO userDTO = convertToDTO(user);
        userDTO.setPassword(null); // Don't return password
        return userDTO;
    }
    
    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        UserDTO userDTO = convertToDTO(user);
        userDTO.setPassword(null); // Don't return password
        return userDTO;
    }
    
    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        // Check if email is being changed and if it already exists
        if (!existingUser.getEmail().equals(userDTO.getEmail()) && 
            userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        
        existingUser.setName(userDTO.getName());
        existingUser.setEmail(userDTO.getEmail());
        
        // Update password only if provided
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        
        User updatedUser = userRepository.save(existingUser);
        
        // Don't return password in response
        UserDTO responseDTO = convertToDTO(updatedUser);
        responseDTO.setPassword(null);
        return responseDTO;
    }
    
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
    
    @Override
    public boolean isUserExists(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    public UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword()
        );
    }
    
    @Override
    public User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        return user;
    }
}