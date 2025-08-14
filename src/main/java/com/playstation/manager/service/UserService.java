package com.playstation.manager.service;

import com.playstation.manager.dto.UserDTO;
import com.playstation.manager.entity.User;
import com.playstation.manager.repository.UserRepository;
import com.playstation.manager.utilz.AuthUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthUtil authUtil;


    public User createAdmin(User user) {
        User currentUser =  getCurrentUser();
        if(!authUtil.isAdmin(currentUser)) {
            return null;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username).orElse(null);
    }
    public User update(Long id, UserDTO dto) {
        User currentUser =  getCurrentUser();
        if(!authUtil.isAdmin(currentUser)) {
            return null;
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setRole(dto.getRole());
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void delete(Long id) {
        User currentUser =  getCurrentUser();
        if(authUtil.isAdmin(currentUser)) {
            userRepository.deleteById(id);
        }

    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public User create(UserDTO dto) {
       User currentUser =  getCurrentUser();
       if(!authUtil.isAdmin(currentUser)) {
           return null;
       }
       if(userRepository.findByUsername(dto.getUsername()).isPresent()) {
           return null;
       }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setRole(dto.getRole());
        return userRepository.save(user);
    }
}
