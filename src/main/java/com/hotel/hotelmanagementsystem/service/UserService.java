package com.hotel.hotelmanagementsystem.service;

import com.hotel.hotelmanagementsystem.exception.DuplicateResourceException;
import com.hotel.hotelmanagementsystem.exception.ResourceNotFoundException;
import com.hotel.hotelmanagementsystem.model.User;
import com.hotel.hotelmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // --- User Registration ---
    @Transactional
    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateResourceException("User", "email", user.getEmail());
        }
        // Set default role for new users
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER"); // Default role for new registrations
        }
        // In a real application, you would hash the password here (e.g., using BCryptPasswordEncoder)
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // --- User Login ---
    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        
        // In a real application, you would compare hashed passwords
        // if (!passwordEncoder.matches(password, user.getPassword())) {
        if (!user.getPassword().equals(password)) { // Simplified for this example
            throw new IllegalArgumentException("Invalid credentials");
        }
        return user;
    }

    // --- Get All Users ---
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // --- Get User by ID ---
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    // --- Get User by Email ---
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    // --- Update User ---
    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (userRepository.findByEmail(userDetails.getEmail()).isPresent() &&
            !userRepository.findByEmail(userDetails.getEmail()).get().getId().equals(id)) {
            throw new DuplicateResourceException("User", "email", userDetails.getEmail());
        }

        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        // For password and role, you'd typically have separate dedicated endpoints/logic
        // user.setPassword(userDetails.getPassword()); // Only if you hash it
        // user.setRole(userDetails.getRole());

        return userRepository.save(user);
    }

    // --- Delete User ---
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        userRepository.deleteById(id);
    }
}
