package com.epam.managemymoney.service;

//import com.epam.managemymoney.model.User;
//import com.epam.managemymoney.repository.UserRepository;
//import com.epam.managemymoney.security.AuthenticationRequest;
//import com.epam.managemymoney.security.AuthenticationResponse;
//import com.epam.managemymoney.security.JwtService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;

//@Service
//@RequiredArgsConstructor
public class AuthenticationService {
   /* private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );
        
        var user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        
        var jwtToken = jwtService.generateToken(user);
        
        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }

    public AuthenticationResponse register(AuthenticationRequest request) {
        var user = User.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .build();

        userRepository.save(user);
        
        var jwtToken = jwtService.generateToken(user);
        
        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }*/
}