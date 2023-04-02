package com.foodunzo.surajnarayanraut.services;

import com.foodunzo.surajnarayanraut.entity.Role;
import com.foodunzo.surajnarayanraut.entity.User;
import com.foodunzo.surajnarayanraut.repository.UserRepository;
import com.foodunzo.surajnarayanraut.utils.AuthenticationRequest;
import com.foodunzo.surajnarayanraut.utils.AuthenticationResponse;
import com.foodunzo.surajnarayanraut.utils.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private  final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
    User user= User.builder()
            .firstName(request.getFirstname())
            .lastName(request.getLastname())
            .password(passwordEncoder.encode(request.getPassword()))
            .email(request.getEmail())
            .role(Role.USER)
            .build();
    userRepository.save(user);
    String jwtToken=jwtService.generateToken(user);
    return AuthenticationResponse.builder()
            .token(jwtToken).build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
    new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        var user =userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken=jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken)
                .build();
    }
}
