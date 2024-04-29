package edu.com.librarymanagementsystem.security;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;
    
    public AuthService(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }
}
