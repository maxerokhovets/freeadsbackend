package com.nucldev.freeads.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import com.nucldev.freeads.entities.User;
import com.nucldev.freeads.repositories.UserRepository;

@Component
public class CustomAuthProvider implements AuthenticationProvider {
    
    @Autowired
    private UserRepository userRepository;
    
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken = null;
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = userRepository.findByUsername(username);
        if (user!=null) {
            if (username.equals(user.getUsername()) && BCrypt.checkpw(password, user.getPassword())) {
                Collection<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(user);
                authenticationToken = new UsernamePasswordAuthenticationToken(
                         new org.springframework.security.core.userdetails.User(username, password, grantedAuthorities), password, grantedAuthorities);
            }
        }else {
            throw new UsernameNotFoundException("Username"+username+"not found!");
        }
        return authenticationToken;
    }
    
    private Collection<GrantedAuthority> getGrantedAuthorities(User user){
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        if (user.getRole().equals("admin")) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }else {
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));    
        }   
        return grantedAuthorities;          
    }

    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

