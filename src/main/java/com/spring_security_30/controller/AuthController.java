package com.spring_security_30.controller;

import com.spring_security_30.entity.Role;
import com.spring_security_30.entity.User;
import com.spring_security_30.payload.LoginDto;
import com.spring_security_30.payload.SignUpDto;
import com.spring_security_30.repository.RoleRepository;
import com.spring_security_30.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;



    @PostMapping("/signin")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(),
                loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);


        return new ResponseEntity<>("User signed-in successfully!. ", HttpStatus.OK);

    }

    @PostMapping("signup")

    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){

        if (userRepository.existsByUsername(signUpDto.getUsername())){
            return  new ResponseEntity<>("User is already taken!. ",HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpDto.getEmail())){
            return new ResponseEntity<>("Email is already taken !. ",HttpStatus.BAD_REQUEST);
        }

        User user=new User();
        user.setName(signUpDto.getName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        Role roles= roleRepository.findByName("ADMIN").get();
        user.setRoles(Collections.singleton(roles));
        userRepository.save(user);
        return  new ResponseEntity<>("User registered successfully ", HttpStatus.OK);
    }


}
