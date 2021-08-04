package com.nucldev.freeads.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nucldev.freeads.payload.CurrentUserSummary;
import com.nucldev.freeads.repositories.UserRepository;
import com.nucldev.freeads.security.CurrentUser;
import com.nucldev.freeads.security.UserPrincipal;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/me")
    public CurrentUserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        
        CurrentUserSummary userSummary = new CurrentUserSummary(currentUser.getId(), currentUser.getUsername(), 
                currentUser.getEmail(), currentUser.getRegistrationDate(), currentUser.getAddsCount());
        
        return userSummary;
    }
    
    
}
