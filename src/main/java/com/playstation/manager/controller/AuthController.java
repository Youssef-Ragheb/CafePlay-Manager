package com.playstation.manager.controller;

import com.playstation.manager.config.JwtUtil;
import com.playstation.manager.dto.AuthRequest;
import com.playstation.manager.dto.AuthResponse;
import com.playstation.manager.entity.Role;
import com.playstation.manager.entity.User;
import com.playstation.manager.service.UserService;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        String token = jwtUtil.generateToken(request.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/sign-up/employee")
    public ResponseEntity<?> signUp(@RequestBody User user) {

        User savedUser = userService.createAdmin(user);
        user.setRole(Role.EMPLOYEE);
        return ResponseEntity.ok(savedUser);
    }
    @PostMapping("/sign-up/admin")
    public ResponseEntity<?> signUp(@RequestBody User user, @PathParam("password") String password) {
        if(!password.equals("AdminHashedPassword")) {
            return ResponseEntity.badRequest().build();
        }
        User savedUser = userService.createAdmin(user);
        user.setRole(Role.ADMIN);
        return ResponseEntity.ok(savedUser);
    }
    @GetMapping("/me")
    public ResponseEntity<?> me() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }
}

