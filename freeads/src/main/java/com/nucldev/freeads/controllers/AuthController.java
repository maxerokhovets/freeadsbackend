package com.nucldev.freeads.controllers;

import java.net.URI;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nucldev.freeads.entities.User;
import com.nucldev.freeads.payload.ApiResponse;
import com.nucldev.freeads.payload.JwtAuthenticationResponse;
import com.nucldev.freeads.payload.LoginRequest;
import com.nucldev.freeads.payload.SignUpRequest;
import com.nucldev.freeads.repositories.UserRepository;
import com.nucldev.freeads.security.JwtTokenProvider;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Autowired
    JwtTokenProvider tokenProvider;
    
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }
    
    
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        
        if(signUpRequest.getUsername().equals("") || signUpRequest.getEmail().equals("")
                || signUpRequest.getPassword().equals("") || signUpRequest.getPassword1().equals("")) {
            return new ResponseEntity(new ApiResponse(false, "Для регистрации заполните все поля!"),
                    HttpStatus.BAD_REQUEST);
            
        }
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Пользователь с таким именем уже существует!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Пользователь с таким адресом электронной почты уже существует!"),
                    HttpStatus.BAD_REQUEST);
        }
        if(!signUpRequest.getEmail().matches(".+@.+")) {
            return new ResponseEntity(new ApiResponse(false, "Введите действительный адрес электронной почты!"),
                    HttpStatus.BAD_REQUEST);
        }
        
        if(!signUpRequest.getPassword().equals(signUpRequest.getPassword1())) {
            return new ResponseEntity(new ApiResponse(false, "Пароли не совпадают!"), HttpStatus.BAD_REQUEST);
        }
        
        // Creating user's account
        User user = new User();

        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole("USER");
        user.setRegistrationDate(new Date());

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();
        return ResponseEntity.created(location).body(new ApiResponse(true, "Ваш аккаунт успешно зарегистрирован!"));
    }
}
